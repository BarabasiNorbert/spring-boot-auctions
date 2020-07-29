package bnorbert.auction.service;

import bnorbert.auction.domain.User;
import bnorbert.auction.domain.VerificationToken;
import bnorbert.auction.repository.UserRepository;
import bnorbert.auction.repository.VerificationTokenRepository;
import bnorbert.auction.transfer.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private VerificationTokenRepository mockVerificationTokenRepository;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userServiceUnderTest = new UserService(mockUserRepository, mockPasswordEncoder, mockVerificationTokenRepository);
    }

    @Test
    void testCreateUser() {

        final SaveUserRequest request = new SaveUserRequest();
        request.setEmail("email@gmail.com");
        request.setPassword("password");
        request.setPasswordConfirm("password");

        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.of(new User()));
        when(mockPasswordEncoder.encode("charSequence")).thenReturn("result");
        when(mockUserRepository.save(new User())).thenReturn(new User());

        final UserResponse result = userServiceUnderTest.createUser(request);
    }

    @Test
    void testCreateUserThatEmailIsTaken() {

        final SaveUserRequest request = new SaveUserRequest();
        request.setEmail("email@gmail.com");
        request.setPassword("password123D.");
        request.setPasswordConfirm("password123D.");

        when(mockUserRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(new User()));
        when(mockPasswordEncoder.encode("charSequence")).thenReturn("result");
        when(mockUserRepository.save(new User())).thenReturn(new User());

        final UserResponse result = userServiceUnderTest.createUser(request);
    }

    @Test
    void testResendToken() {
        final ResendTokenRequest request = new ResendTokenRequest();
        request.setEmail("email");

        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.of(new User()));
        when(mockUserRepository.save(new User())).thenReturn(new User());

        final UserResponse result = userServiceUnderTest.resendToken(request);
    }

    @Test
    void testConfirmUser() {
        final VerifyTokenRequest request = new VerifyTokenRequest();
        request.setVerificationToken("verificationToken");

        final Optional<VerificationToken> verificationToken = Optional.of(new VerificationToken(new User()));
        when(mockVerificationTokenRepository.findByVerificationToken("verificationToken")).thenReturn(verificationToken);

        when(mockUserRepository.save(new User())).thenReturn(new User());

        final UserResponse result = userServiceUnderTest.confirmUser(request);

    }

    @Test
    void testGetUserId() {
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(new User()));

        final UserResponse result = userServiceUnderTest.getUserId(1L);

    }

    @Test
    void testGetUser() {
        final User expectedResult = new User();
        when(mockUserRepository.findById(0L)).thenReturn(Optional.of(new User()));

        final User result = userServiceUnderTest.getUser(0L);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testResetPassword() {
        final ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("password");
        request.setPasswordConfirm("passwordConfirm");
        request.setVerificationToken("verificationToken");

        final Optional<VerificationToken> verificationToken = Optional.of(new VerificationToken(new User()));
        when(mockVerificationTokenRepository.findByVerificationToken("verificationToken")).thenReturn(verificationToken);

        when(mockPasswordEncoder.encode("charSequence")).thenReturn("result");
        when(mockUserRepository.save(new User())).thenReturn(new User());


        final UserResponse result = userServiceUnderTest.resetPassword(request);
    }

    @Test
    void testDeleteUser() {
        userServiceUnderTest.deleteUser(1L);

        // Verify the results
        verify(mockUserRepository).deleteById(1L);
    }

    @Test
    void testGetCurrentUser() {
        final User expectedResult = new User();
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.of(new User()));

        final User result = userServiceUnderTest.getCurrentUser();
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testRemoveNotActivatedUsers() {
        when(mockUserRepository.findAllByEnabledIsFalseAndCreatedDateBefore(Instant.ofEpochSecond(0L))).thenReturn(Collections.singletonList(new User()));
        userServiceUnderTest.removeNotActivatedUsers();

        verify(mockUserRepository).delete(any(User.class));
    }
}
