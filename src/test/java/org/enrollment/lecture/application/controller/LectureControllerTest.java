package org.enrollment.lecture.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enrollment.lecture.application.service.LectureService;
import org.enrollment.lecture.domain.dto.lecture.LectureRequestDto;
import org.enrollment.lecture.domain.dto.lecture.LectureResponseDto;
import org.enrollment.lecture.domain.dto.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.*;
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

    @Test
    void 특강을_신청한다() throws Exception {
        // given
        long lectureId = 1L;
        long userId = 1L;

        LectureRequestDto requestDto = LectureRequestDto.of(lectureId, userId);
        willDoNothing().given(lectureService).applyLecture(requestDto);

        // when
        mockMvc.perform(post("/api/lectures/apply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(LectureRequestDto.of(lectureId, userId)))
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        then(lectureService).should().applyLecture(requestDto);
    }

    @Test
    void 특강목록을_가져온다() throws Exception {
        // given
        List<LectureResponseDto> list =
                List.of(
                        LectureResponseDto.of(1L, "special", 30, LocalDate.now()),
                        LectureResponseDto.of(2L, "special2", 40, LocalDate.now())
                );
        given(lectureService.selectLectures()).willReturn(list);

        // when
        mockMvc.perform(get("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(objectMapper.writeValueAsBytes(Response.success(list))));

        // then
        then(lectureService).should().selectLectures();
    }

    @Test
    void 등록된_특강을_사용자아이디로_조회한다() throws Exception {
        // given
        long userId = 1L;
        boolean result = true;

        given(lectureService.hasUserIdOnLectureUserList(userId)).willReturn(result);

        // when
        mockMvc.perform(get("/api/lectures/application/%d".formatted(userId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().bytes(objectMapper.writeValueAsBytes(Response.success(true))));

        // then
        then(lectureService).should().hasUserIdOnLectureUserList(userId);
    }



}