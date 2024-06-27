package org.enrollment.lecture.infra.repository.enrollmentHistory.impl;

import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.domain.entity.EnrollmentHistory;
import org.enrollment.lecture.domain.repository.EnrollmentHistoryRepository;
import org.enrollment.lecture.infra.repository.enrollmentHistory.EnrollmentHistoryJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EnrollmentHistoryRepositoryImpl implements EnrollmentHistoryRepository {

    private final EnrollmentHistoryJpaRepository enrollmentHistoryJpaRepository;

    @Override
    public EnrollmentHistory save(EnrollmentHistory enrollmentHistory) {
        return enrollmentHistoryJpaRepository.save(enrollmentHistory);
    }
}
