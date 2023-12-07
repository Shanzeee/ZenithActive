package com.brvsk.ZenithActive.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void itShouldGetAllUsers() {
        // Given
        when(userRepository.findAll()).thenReturn(Arrays.asList(
                new User(UUID.randomUUID(), "John", "Doe", "kacpersjusz@gmail.com", Gender.MALE),
                new User(UUID.randomUUID(), "Jane", "Doe","kacpersjuszb@gmail.com", Gender.FEMALE)
        ));

        when(userMapper.mapToResponse(any())).thenReturn(new UserResponse());
        // When
        List<UserResponse> result = userService.getAllUsers();
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void itShouldGetUserById() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(userId, "John", "Doe", "kacpersjuszb@gmail.com", Gender.MALE)));
        when(userMapper.mapToResponse(any())).thenReturn(new UserResponse());
        // When
        UserResponse result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
    }

    @Test
    void itShouldThrowsUserNotFoundException(){
        // Given When
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));

    }

    @Test
    void itShouldCreateUser() {
        // Given
        UserRequest userRequest = new UserRequest("John", "Doe",  Gender.MALE, "kacpersjuszb@gmail.com");
        when(userMapper.mapToResponse(any())).thenReturn(new UserResponse());
        // When
        UserResponse result = userService.createUser(userRequest);
        // Then
        assertNotNull(result);
    }

    @Test
    void itShouldDeleteUser() {
        // Given
        UUID userId = UUID.randomUUID();
        // When
        userService.deleteUser(userId);
        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }
}