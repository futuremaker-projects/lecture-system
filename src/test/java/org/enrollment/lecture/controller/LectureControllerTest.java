package org.enrollment.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enrollment.lecture.controller.dto.enrollment.EnrollmentRequestDto;
import org.enrollment.lecture.controller.dto.enrollment.EnrollmentResponseDto;
import org.enrollment.lecture.controller.dto.lecture.LectureResponseDto;
import org.enrollment.lecture.domain.entity.Enrollment;
import org.enrollment.lecture.domain.entity.Lecture;
import org.enrollment.lecture.domain.entity.LectureInfo;
import org.enrollment.lecture.domain.service.LectureService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LectureController.class)
@ExtendWith(SpringExtension.class)
class LectureControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LectureService lectureService;

    @DisplayName("특강 id와 유저 id를 요청 바디로 받아 특강을 등록한다.")
    @Test
    void givenLectureIdAndUserId_whenRequestingEnrollLecture_thenDoNothing() throws Exception {
        // given
        long lectureId = 1L;
        long userId = 1L;

        EnrollmentRequestDto requestDto = EnrollmentRequestDto.of(lectureId, userId);
        willDoNothing().given(lectureService).enrollLecture(requestDto);

        // when
        mockMvc.perform(post("/api/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(EnrollmentRequestDto.of(lectureId, userId)))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        then(lectureService).should().enrollLecture(requestDto);
    }

    @DisplayName("모든 특강 목록을 반환한다.")
    @Test
    void givenNothing_whenRequestingLectureList_thenReturnsLectureList() throws Exception {
        // given
        LocalDateTime lecture1OpenedAt = LocalDateTime.now();
        LocalDateTime lecture2OpenedAt = LocalDateTime.now();
        List<Lecture> list = List.of(
                Lecture.of(1L, 30, LectureInfo.of(1L, "lecture1", lecture1OpenedAt)),
                Lecture.of(2L, 40, LectureInfo.of(2L, "lecture2", lecture2OpenedAt))
        );
        List<LectureResponseDto> responseDtos = list.stream().map(LectureResponseDto::from).toList();
        given(lectureService.selectLectures()).willReturn(responseDtos);

        // when
        mockMvc.perform(get("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(objectMapper.writeValueAsBytes(responseDtos)));

        // then
        then(lectureService).should().selectLectures();
    }

    @DisplayName("유저 id를 받아 특강목록을 요청하면 등록된 특강목록을 반환한다.")
    @Test
    void givenUserId_whenRequestingEnrolledLecturesByUserId_thenReturnsEnrollmentResponseDtoList() throws Exception {
        // given
        long userId = 1L;

        List<EnrollmentResponseDto> responseDtos = enrollments().stream().map(EnrollmentResponseDto::from).toList();
        given(lectureService.selectAllEnrolledLecturesByUserId(userId)).willReturn(responseDtos);

        // when
        mockMvc.perform(get("/api/lectures/application/%d".formatted(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(objectMapper.writeValueAsBytes(responseDtos)));

        // then
        then(lectureService).should().selectAllEnrolledLecturesByUserId(userId);
    }

    private List<Enrollment> enrollments() {
        return List.of(
                Enrollment.of(1L, 1L, 1L),
                Enrollment.of(2L, 1L, 2L),
                Enrollment.of(3L, 1L, 3L),
                Enrollment.of(4L, 1L, 4L),
                Enrollment.of(4L, 1L, 5L)
        );
    }
}