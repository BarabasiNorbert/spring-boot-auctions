package bnorbert.auction.service;

import bnorbert.auction.domain.User;
import bnorbert.auction.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
        when(mockUserRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(new User()));

        final UserDetails result = myUserDetailsServiceUnderTest.loadUserByUsername("email@gmail.com");
    }

    @Test
    void testLoadUserByUsername_ThrowsUsernameNotFoundException() {
        when(mockUserRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> {
            myUserDetailsServiceUnderTest.loadUserByUsername("email@gmail.com");
        }).isInstanceOf(UsernameNotFoundException.class).hasMessageContaining("message");
    }
}
