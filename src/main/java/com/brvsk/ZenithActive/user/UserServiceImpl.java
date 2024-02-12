package com.brvsk.ZenithActive.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

}
