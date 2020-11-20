package bnorbert.auction.service;

import bnorbert.auction.domain.User;
import bnorbert.auction.exception.ResourceNotFoundException;
import bnorbert.auction.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class MyUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private MyUserDetailsService myUserDetailsServiceUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
        myUserDetailsServiceUnderTest = new MyUserDetailsService(mockUserRepository);
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setEmail("user@atnotnull.com");
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);

        when(mockUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        final UserDetails result = myUserDetailsServiceUnderTest.loadUserByUsername(user.getEmail());
    }

    @Test
    void testLoadUserByUsername_ThrowsUsernameNotFoundException() {

        User user = new User();
        user.setEmail("user@atnotnull.com");
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);

        when(mockUserRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> myUserDetailsServiceUnderTest.loadUserByUsername("user@gmail.com")).isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("message");
    }
}
