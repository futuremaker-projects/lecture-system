package org.enrollment.lecture.infra.repository.userAccount.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.domain.entity.UserAccount;
import org.enrollment.lecture.infra.repository.userAccount.UserAccountJpaRepository;
import org.enrollment.lecture.infra.repository.userAccount.UserAccountRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountJpaRepository userAccountJpaRepository;

    @Override
    public UserAccount findById(Long id) {
        return userAccountJpaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("user not found : id - %d".formatted(id)));
    }
}
