package bnorbert.auction.service;

import bnorbert.auction.domain.User;
import bnorbert.auction.domain.VerificationToken;
import bnorbert.auction.repository.UserRepository;
import bnorbert.auction.repository.VerificationTokenRepository;
import bnorbert.auction.transfer.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private VerificationTokenRepository mockVerificationTokenRepository;
    @Mock
    private JavaMailSender mockMailSender;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userServiceUnderTest = new UserService(mockUserRepository, mockPasswordEncoder, mockVerificationTokenRepository, mockMailSender);
    }

    @Test
    void testCreateUser() {

        final SaveUserRequest request = new SaveUserRequest();
        request.setEmail("email@gmail.com");
        request.setPassword("password");
        request.setPasswordConfirm("password");

        when(mockUserRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        when(mockPasswordEncoder.encode("charSequence")).thenReturn("result");
        when(mockUserRepository.save(new User())).thenReturn(new User());

        final UserResponse result = userServiceUnderTest.createUser(request);

        verify(mockMailSender).send(new SimpleMailMessage());
    }

    @Test
    void testCreateUser_JavaMailSenderThrowsMailException() {

        final SaveUserRequest request = new SaveUserRequest();
        request.setEmail("email@gmail.com");
        request.setPassword("password");
        request.setPasswordConfirm("password");

        when(mockUserRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(new User()));
        when(mockPasswordEncoder.encode("charSequence")).thenReturn("result");
        doThrow(MailException.class).when(mockMailSender).send(new SimpleMailMessage());
        when(mockUserRepository.save(new User())).thenReturn(new User());


        final UserResponse result = userServiceUnderTest.createUser(request);
    }

    @Test
    void testResendToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        final ResendTokenRequest request = new ResendTokenRequest();
        request.setEmail(user.getEmail());

        when(mockUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mockUserRepository.save(user)).thenReturn(user);

        final UserResponse result = userServiceUnderTest.resendToken(request);
    }

    @Test
    void testConfirmUser() {

        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        final VerifyTokenRequest request = new VerifyTokenRequest();
        request.setVerificationToken("verificationToken");

        final Optional<VerificationToken> verificationToken = Optional.of(new VerificationToken(user));
        when(mockVerificationTokenRepository.findByVerificationToken(request.getVerificationToken())).thenReturn(verificationToken);

        when(mockUserRepository.save(user)).thenReturn(user);

        final UserResponse result = userServiceUnderTest.confirmUser(request);

    }

    @Test
    void testGetUserId() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        final UserResponse result = userServiceUnderTest.getUserId(user.getId());

    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        when(mockUserRepository.findById(user.getId())).thenReturn(Optional.of(user));

        final User result = userServiceUnderTest.getUser(user.getId());
        assertThat(result).isEqualTo(user);
    }

    @Test
    void testResetPassword() {

        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        final ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("password");
        request.setPasswordConfirm("password");
        request.setVerificationToken("verificationToken");

        final Optional<VerificationToken> verificationToken = Optional.of(new VerificationToken(user));
        when(mockVerificationTokenRepository.findByVerificationToken("verificationToken")).thenReturn(verificationToken);

        when(mockPasswordEncoder.encode("charSequence")).thenReturn("result");
        when(mockUserRepository.save(user)).thenReturn(user);


        final UserResponse result = userServiceUnderTest.resetPassword(request);
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        userServiceUnderTest.deleteUser(user.getId());

        verify(mockUserRepository).deleteById(1L);
    }

    @Test
    void testRemoveNotActivatedUsers() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@atnotnull.com");

        when(mockUserRepository.findAllByEnabledIsFalseAndCreatedDateBefore(Instant.now())).thenReturn(Collections.singletonList(user));
        userServiceUnderTest.removeNotActivatedUsers();

        verify(mockUserRepository).delete(any(User.class));
    }
}
