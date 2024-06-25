package org.enrollment.lecture.infra.repository.enrollment.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.domain.entity.Enrollment;
import org.enrollment.lecture.infra.exception.ApplicationException;
import org.enrollment.lecture.infra.exception.ErrorCode;
import org.enrollment.lecture.infra.repository.enrollment.EnrollmentJpaRepository;
import org.enrollment.lecture.infra.repository.enrollment.EnrollmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private final EnrollmentJpaRepository enrollmentJpaRepository;

    @Override
    public Enrollment findById(Long id) {
        return enrollmentJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("enrollment not found : id - %d".formatted(id)));
    }

    @Override
    public List<Enrollment> findAllByLectureId(Long lectureId) {
        return enrollmentJpaRepository.findAllByLectureId(lectureId);
    }

    @Override
    public Enrollment findByUserId(Long userId) {
        if (userId == null) {
            throw new ApplicationException(ErrorCode.CONTENT_NOT_FOUND, "userId is null");
        }
        return enrollmentJpaRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        return enrollmentJpaRepository.save(enrollment);
    }

}
