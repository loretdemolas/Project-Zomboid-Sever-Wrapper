package org.loretdemolas.pzsw.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.loretdemolas.pzsw.dto.LoginUserDTO;
import org.loretdemolas.pzsw.entity.User;
import org.loretdemolas.pzsw.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private LoginUserDTO loginUserDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername("testuser");
        loginUserDTO.setPassword("testpassword");

        user = new User();
        user.setUsername("testuser");
    }

    @Test
    void testAuthenticateSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                "testuser", "testpassword");
        when(authenticationManager.authenticate(authToken)).thenReturn(authToken);

        User result = authenticationService.authenticate(loginUserDTO);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testAuthenticateFailure() {
        lenient().when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                "testuser", "testpassword");
        when(authenticationManager.authenticate(authToken)).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.authenticate(loginUserDTO));
    }
}
