package org.enrollment.lecture.controller.dto.enrollment;

import org.enrollment.lecture.domain.entity.Enrollment;

public record EnrollmentRequestDto(long lectureId, long userId) {

    public static EnrollmentRequestDto of(long lectureId, long userIdt) {
        return new EnrollmentRequestDto(lectureId, userIdt);
    }

    public Enrollment to() {
        return Enrollment.of(userId, lectureId);
    }

}
