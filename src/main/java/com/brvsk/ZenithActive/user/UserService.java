package com.brvsk.ZenithActive.user;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(UUID userId);
    void deleteUser(UUID userId);
    void uploadUserProfileImage(UUID userId, MultipartFile file);
    byte[] getUserProfileImage(UUID userId);
}
