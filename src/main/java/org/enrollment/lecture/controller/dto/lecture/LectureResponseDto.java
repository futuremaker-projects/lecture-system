package org.enrollment.lecture.controller.dto.lecture;

import org.enrollment.lecture.controller.dto.lectureinfo.LectureInfoResponseDto;
import org.enrollment.lecture.domain.entity.Lecture;

import java.time.LocalDateTime;

public record LectureResponseDto(
        Long id,
        int userLimit,
        LectureInfoResponseDto lectureInfoDto,
        LocalDateTime openedAt
) {
    public static LectureResponseDto of(Long id,
                                        int userLimit,
                                        LectureInfoResponseDto lectureInfoResponseDto,
                                        LocalDateTime openedAt) {
        return new LectureResponseDto(id, userLimit, lectureInfoResponseDto, openedAt);
    }

    public static LectureResponseDto of(Long id,
                                        int userLimit,
                                        LocalDateTime openedAt) {
        return new LectureResponseDto(id, userLimit, null, openedAt);
    }


    public static LectureResponseDto from(Lecture lecture) {
        return LectureResponseDto.of(
                lecture.getId(),
                lecture.getUserLimit(),
                LectureInfoResponseDto.from(lecture.getLectureInfo()),
                lecture.getOpenedAt()
        );
    }
}
