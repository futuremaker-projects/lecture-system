package org.enrollment.lecture.controller;

import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.controller.dto.enrollment.EnrollmentRequestDto;
import org.enrollment.lecture.controller.dto.enrollment.EnrollmentResponseDto;
import org.enrollment.lecture.controller.dto.lecture.LectureResponseDto;
import org.enrollment.lecture.domain.service.LectureService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /**
     * 특강 신청 **API `POST /lectures/apply`**
     */
    @PostMapping(value = "/apply", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> enrollLecture(@RequestBody EnrollmentRequestDto requestDto) {
        lectureService.enrollLecture(requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 특강 목록 API `GET /lectures`**
     */
    @GetMapping
    public ResponseEntity<List<LectureResponseDto>> selectLectures() {
        return ResponseEntity.ok(lectureService.selectLectures());
    }

    /**
     * 특강 신청 완료 여부 조회 API **`GET /lectures/application/{userId}`**
     */
    @GetMapping("/application/{userId}")
    public ResponseEntity<List<EnrollmentResponseDto>> verifyAppliedLecture(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(lectureService.hasUserIdOnLectureUserList(userId));
    }

}
