package com.brvsk.ZenithActive.user;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID userId);
    UserResponse createUser(UserRequest userRequest);
    void deleteUser(UUID userId);
}
