package org.enrollment.lecture.controller.dto.enrollment;

import org.enrollment.lecture.domain.entity.Enrollment;

import java.time.LocalDateTime;

public record EnrollmentResponseDto(
        Long id, Long lectureId, Long userId, LocalDateTime enrolledAt
) {
    public static EnrollmentResponseDto of(Long id, Long lectureId, Long userId, LocalDateTime enrolledAt) {
        return new EnrollmentResponseDto(id, lectureId, userId, enrolledAt);
    }

    public static EnrollmentResponseDto from(Enrollment enrollment) {
        return new EnrollmentResponseDto(
                enrollment.getId(),
                enrollment.getLectureId(),
                enrollment.getUserId(),
                enrollment.getEnrolledAt()
        );
    }
}
