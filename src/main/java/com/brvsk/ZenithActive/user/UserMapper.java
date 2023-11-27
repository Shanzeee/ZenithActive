package com.brvsk.ZenithActive.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse mapToResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender().toString())
                .build();
    }
}
