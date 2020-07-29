package bnorbert.auction.service;

import bnorbert.auction.domain.Role;
import bnorbert.auction.domain.User;
import bnorbert.auction.domain.VerificationToken;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.exception.UserAlreadyExistException;
import bnorbert.auction.repository.UserRepository;
import bnorbert.auction.repository.VerificationTokenRepository;
import bnorbert.auction.transfer.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.mailSender = mailSender;
    }

    private boolean emailExists(final String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public UserResponse createUser(SaveUserRequest request){
        LOGGER.info("Creating user: {}", request);
        if (emailExists(request.getEmail())) {
            throw new UserAlreadyExistException("That email is taken. Try another."); }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);
        user.setCreatedDate(Instant.now());

        VerificationToken verificationToken = new VerificationToken(user);
        user.addToken(verificationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("localhost");
        mailMessage.setTo(request.getEmail());
        mailMessage.setText(verificationToken.getVerificationToken());

        mailSender.send(mailMessage);

        User savedUser = userRepository.save(user);
        return mapUserResponse(savedUser);
    }

    private UserResponse mapUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());

        for (Role role : user.getRoles()) {
            RoleResponse roleResponse = new RoleResponse();
            roleResponse.setId(role.getId());
            roleResponse.setName(role.getName());

            userResponse.getRoles().add(roleResponse);
        }
        return userResponse;
    }

    @Transactional
    public UserResponse resendToken(ResendTokenRequest request) {
        User user = userRepository.
                findByEmail(request.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User " + request.getEmail() + " not found."));

        VerificationToken verificationToken = new VerificationToken(user);
        user.addToken(verificationToken);


        User savedUser = userRepository.save(user);
        return mapUserResponse(savedUser);
    }

    @Transactional
    public UserResponse confirmUser(VerifyTokenRequest request) {
        VerificationToken verificationToken = verificationTokenRepository
                .findByVerificationToken(request.getVerificationToken()).orElseThrow(() ->
                        new ResourceNotFoundException("Token " + request.getVerificationToken() + " not found."));
        User user = verificationToken.getUser();
        user.setEnabled(true);

        if (currentTime().isAfter(verificationToken.getExpirationDate())){
            throw new ResourceNotFoundException("Token " + request.getVerificationToken() + "has expired");
        }

        User savedUser = userRepository.save(user);
        return mapUserResponse(savedUser);
    }


    @Transactional
    public UserResponse getUserId(long userId) {
        LOGGER.info("Retrieving user {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User" + userId + " id not found."));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());

        for (Role role : user.getRoles()) {
            RoleResponse roleResponse = new RoleResponse();
            roleResponse.setId(role.getId());
            roleResponse.setName(role.getName());

            userResponse.getRoles().add(roleResponse);
        }
            return userResponse;
    }

    public User getUser(long id) {
        LOGGER.info("Retrieving user {}", id);
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User" + id + " not found."));
    }

    @Transactional
    public UserResponse resetPassword(ResetPasswordRequest request) {
        LOGGER.info("Changing password for user {}", request);
        VerificationToken verificationToken = verificationTokenRepository
                .findByVerificationToken(request.getVerificationToken()).orElseThrow(() ->
                        new ResourceNotFoundException("Token " + request.getVerificationToken() + " not found."));
        User user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPasswordConfirm(passwordEncoder.encode(request.getPasswordConfirm()));

        if (currentTime().isAfter(verificationToken.getExpirationDate())){
            throw new ResourceNotFoundException("Token " + request.getVerificationToken() + "has expired");
        }

        User savedUser = userRepository.save(user);
        return mapUserResponse(savedUser);
    }

    public void deleteUser(long id){
        LOGGER.info("Deleting user {}", id);
        userRepository.deleteById(id);
    }

    @Transactional
    public User getCurrentUser() {
       User principal = (User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User email not found - " + principal.getEmail()));
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    //ON DELETE cascade(foreign keys) - ConstraintViolationException
    @Scheduled(cron = "0 0 1 * * ?")//At 01:00 am everyday.
    //@Scheduled(cron = "0 0 0 * * ?")//Every day at midnight - 12am
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByEnabledIsFalseAndCreatedDateBefore(Instant.now().minus(5, ChronoUnit.DAYS))
                .forEach(userRepository::delete);
        LOGGER.info("Schedule: Deleting not activated users");
    }

    private LocalDateTime currentTime() { return LocalDateTime.now(); }

}
