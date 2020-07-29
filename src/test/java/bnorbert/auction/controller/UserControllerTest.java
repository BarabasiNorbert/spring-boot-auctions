package bnorbert.auction.controller;

import bnorbert.auction.domain.User;
import bnorbert.auction.service.UserService;
import bnorbert.auction.transfer.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class UserControllerTest {

    @Mock
    private UserService mockUserService;

    private UserController userControllerUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        userControllerUnderTest = new UserController(mockUserService);
    }

    @Test
    void testCreateUser() {

        final SaveUserRequest request = new SaveUserRequest();
        request.setEmail("email");
        request.setPassword("password");
        request.setPasswordConfirm("passwordConfirm");

        final UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("email");
        userResponse.setPassword("password");

        final RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(1L);
        roleResponse.setName("name");
        userResponse.setRoles(new HashSet<>(Collections.singletonList(roleResponse)));
        when(mockUserService.createUser(any(SaveUserRequest.class))).thenReturn(userResponse);


        final ResponseEntity<UserResponse> result = userControllerUnderTest.createUser(request);

    }

    @Test
    void testGetUserId() {

        final UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("email@gmail.com");
        userResponse.setPassword("password");

        final RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(1L);
        roleResponse.setName("name");
        userResponse.setRoles(new HashSet<>(Collections.singletonList(roleResponse)));
        when(mockUserService.getUserId(1L)).thenReturn(userResponse);


        final ResponseEntity<UserResponse> result = userControllerUnderTest.getUserId(0L);
    }

    @Test
    void testConfirmUser() {

        final VerifyTokenRequest request = new VerifyTokenRequest();
        request.setVerificationToken("verificationToken");

        final UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("email@gmail.com");
        userResponse.setPassword("password");

        final RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(1L);
        roleResponse.setName("name");
        userResponse.setRoles(new HashSet<>(Collections.singletonList(roleResponse)));
        when(mockUserService.confirmUser(any(VerifyTokenRequest.class))).thenReturn(userResponse);


        final ResponseEntity<UserResponse> result = userControllerUnderTest.confirmUser(request);

    }

    @Test
    void testResendToken() {
        final ResendTokenRequest request = new ResendTokenRequest();
        request.setEmail("email");

        final UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("email@gmail.com");
        userResponse.setPassword("password");

        final RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(1L);
        roleResponse.setName("name");
        userResponse.setRoles(new HashSet<>(Collections.singletonList(roleResponse)));
        when(mockUserService.resendToken(any(ResendTokenRequest.class))).thenReturn(userResponse);

        final ResponseEntity<UserResponse> result = userControllerUnderTest.resendToken(request);
    }

    @Test
    void testGetUser() {
        final ResponseEntity<User> expectedResult = new ResponseEntity<>(new User(), HttpStatus.CONTINUE);
        when(mockUserService.getUser(1L)).thenReturn(new User());

        final ResponseEntity<User> result = userControllerUnderTest.getUser(1L);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testResetPassword() {

        final ResetPasswordRequest request = new ResetPasswordRequest();
        request.setPassword("password");
        request.setPasswordConfirm("passwordConfirm");
        request.setVerificationToken("verificationToken");

        final UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("email@gmail.com");
        userResponse.setPassword("password");

        final RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(1L);
        roleResponse.setName("name");
        userResponse.setRoles(new HashSet<>(Collections.singletonList(roleResponse)));
        when(mockUserService.resetPassword(any(ResetPasswordRequest.class))).thenReturn(userResponse);


        final ResponseEntity<UserResponse> result = userControllerUnderTest.resetPassword(request);
    }

    @Test
    void testDeleteUser() {
        final ResponseEntity result = userControllerUnderTest.deleteUser(1L);

        // Verify the results
        verify(mockUserService).deleteUser(1L);
    }
}
