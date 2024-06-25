package org.enrollment.lecture.domain.dto.lecture;

import org.enrollment.lecture.domain.entity.Lecture;

import java.time.LocalDate;

public record LectureResponseDto(
        Long id,
        String name,
        int limitUser,
        LocalDate openedAt
) {
    public static LectureResponseDto of(Long id, String name, int limitUser, LocalDate openedAt) {
        return new LectureResponseDto(id, name, limitUser, openedAt);
    }

    public static LectureResponseDto from(Lecture lecture) {
        return LectureResponseDto.of(
                lecture.getId(),
                lecture.getName(),
                lecture.getLimitUser(),
                lecture.getOpenedAt()
        );
    }
}
