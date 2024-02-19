package com.brvsk.ZenithActive.user;


import com.brvsk.ZenithActive.excpetion.S3FileNotFound;
import com.brvsk.ZenithActive.excpetion.UserNotFoundException;
import com.brvsk.ZenithActive.s3.S3Buckets;
import com.brvsk.ZenithActive.s3.S3Service;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;

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

    @Override
    public void uploadUserProfileImage(UUID userId, MultipartFile file) {
        checkIfUserExistsOrThrow(userId);

        String profileImageId = UUID.randomUUID().toString();
        try {
            s3Service.putObject(
                    s3Buckets.getUser(),
                    "profile-images/%s/%s".formatted(userId, profileImageId),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException("failed to upload profile image", e);
        }
        userRepository.updateCustomerProfileImageId(userId,profileImageId);
    }

    @Override
    public byte[] getUserProfileImage(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (StringUtils.isBlank(user.getProfileImageId())) {
            throw new S3FileNotFound(user.getProfileImageId());
        }

        String pdfKey = String.format("profile-images/%s/%s", userId, user.getProfileImageId());

        try{
            return s3Service.getObject(
                    s3Buckets.getUser(),
                    pdfKey);

        } catch (Exception e) {
            throw new S3FileNotFound(pdfKey);
        }
    }

    private void checkIfUserExistsOrThrow(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

}
