package org.enrollment.lecture.controller.dto.lectureinfo;

import org.enrollment.lecture.domain.entity.LectureInfo;

import java.time.LocalDateTime;

public record LectureInfoResponseDto(
        Long id, String name, LocalDateTime createdAt
) {
    public static LectureInfoResponseDto of(Long id, String name, LocalDateTime createdAt) {
        return new LectureInfoResponseDto(id, name, createdAt);
    }

    public static LectureInfoResponseDto of(Long id, String name) {
        return new LectureInfoResponseDto(id, name, LocalDateTime.now());
    }

    public static LectureInfoResponseDto from(LectureInfo lectureInfo) {
        return LectureInfoResponseDto.of(
                lectureInfo.getId(), lectureInfo.getName(), lectureInfo.getOpenedAt()
        );
    }

}
