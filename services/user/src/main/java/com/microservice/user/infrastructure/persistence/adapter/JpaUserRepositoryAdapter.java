package com.microservice.user.infrastructure.persistence.adapter;

import com.microservice.user.application.port.out.UserRepository;
import com.microservice.user.domain.User;
import com.microservice.user.infrastructure.persistence.entity.UserEntity;
import com.microservice.user.infrastructure.persistence.mapper.UserMapper;
import com.microservice.user.infrastructure.persistence.repository.UserPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserPersistenceRepository repository;

    public JpaUserRepositoryAdapter(UserPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User data) {
        UserEntity userEntity = UserMapper.toPersistence(data);
        UserEntity userSaved = this.repository.save(userEntity);
        return UserMapper.toDomain(userSaved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntityOptional = this.repository.findByEmail(email);
        return userEntityOptional.map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> userEntityOptional = this.repository.findById(id);
        return userEntityOptional.map(UserMapper::toDomain);
    }

    @Override
    public User update(User data) {
        return null;
    }
}
