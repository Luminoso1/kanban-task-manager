package com.microservice.user.infrastructure.persistence.mapper;

import com.microservice.user.application.dto.UserProfileDto;
import com.microservice.user.application.dto.UserResponse;
import com.microservice.user.domain.User;
import com.microservice.user.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity entity){
        return new User(
                entity.getId(),
                entity.getProviderId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getVerified()
        );
    }

    public static UserEntity toPersistence(User user){
        UserEntity entity = new UserEntity();
        entity.setId(user.id());
        entity.setProviderId(user.providerId());
        entity.setEmail(user.email());
        entity.setPassword(user.password());
        entity.setVerified(user.verified());
        return entity;
    }

    public static UserResponse toResponse(User user){
        return new UserResponse(
                user.id(),
                user.providerId(),
                user.email(),
                user.password(),
                user.verified()
        );
    }

    public static UserProfileDto toProfile(User user){
        return new UserProfileDto(
                user.id(),
                user.providerId(),
                user.email(),
                user.verified()
        );
    }
}
