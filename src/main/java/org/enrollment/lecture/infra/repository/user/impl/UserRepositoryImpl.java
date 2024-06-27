package org.enrollment.lecture.infra.repository.user.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.domain.entity.User;
import org.enrollment.lecture.infra.repository.user.UserJpaRepository;
import org.enrollment.lecture.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(Long id) {
        return userJpaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("user not found : id - %d".formatted(id)));
    }
}
