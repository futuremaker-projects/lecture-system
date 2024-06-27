package org.enrollment.lecture.domain.service;

import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.controller.dto.enrollment.EnrollmentRequestDto;
import org.enrollment.lecture.controller.dto.enrollment.EnrollmentResponseDto;
import org.enrollment.lecture.controller.dto.lecture.LectureResponseDto;
import org.enrollment.lecture.domain.entity.Enrollment;
import org.enrollment.lecture.domain.entity.EnrollmentHistory;
import org.enrollment.lecture.domain.entity.Lecture;
import org.enrollment.lecture.domain.entity.User;
import org.enrollment.lecture.domain.repository.EnrollmentHistoryRepository;
import org.enrollment.lecture.domain.repository.EnrollmentRepository;
import org.enrollment.lecture.domain.repository.LectureRepository;
import org.enrollment.lecture.domain.repository.UserRepository;
import org.enrollment.lecture.infra.exception.ApplicationException;
import org.enrollment.lecture.infra.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentHistoryRepository enrollmentHistoryRepository;

    /**
     * lecture_id와 user_id를 받아 특강을 등록한다.
     *  - 이미 특강에 등록된 유저는 같은 강의 등록시도시 예외를 발생시킨다.
     *  - 특강의 정해진 정원이상 등록시도시 예외를 발생시킨다. (특강마다 정원은 다르며 기본정원은 30명이다)
     */
    @Transactional
    public void enrollLecture(EnrollmentRequestDto requestDto) {
        Lecture lecture = lectureRepository.findById(requestDto.lectureId());
        User user = userRepository.findById(requestDto.userId());
        List<Enrollment>  enrolledLectures = enrollmentRepository.findAllByLectureId(requestDto.lectureId());

        Enrollment enrollment = Enrollment.of(lecture.getId(), user.getId());
        // 특강을 틍록하고자하는 유저가 이미 특강을 등록했다면 예외를 발생시킨다.
        enrollment.checkIfExistedUserId(enrolledLectures, user.getId());
        // 특정 특강의 정해진 정원초과시 예외를 발생시킨다.
        lecture.checkIfExceededUserLimit(enrolledLectures);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        enrollmentHistoryRepository.save(EnrollmentHistory.of(savedEnrollment));
    }

    /**
     * 특강목록을 조회시 모든 특강을 반환한다.
     *  - 특강은 특강정보를 가진다. 목록은 아래와 같다.
     *   List.of(
     *            Lecture.of(1L, 30, LectureInfo.of(1L, "lecture1", LocalDateTime.now())),
     *            Lecture.of(2L, 40, LectureInfo.of(2L, "lecture2", LocalDateTime.now()))
     *         );
     */
    public List<LectureResponseDto> selectLectures() {
        return lectureRepository.findAll().stream()
                .map(LectureResponseDto::from)
                .toList();
    }

    /**
     * 특정 유저 id로 해당 유저가 등록한 모든 특강 목록을 조회한다.
     *   - 등록한 특강이 없을시 예외를 발생시킨다.
     */
    public List<EnrollmentResponseDto> hasUserIdOnLectureUserList(Long userId) {
        List<EnrollmentResponseDto> list = enrollmentRepository.findAllByUserId(userId).stream()
                .map(EnrollmentResponseDto::from)
                .toList();
        if (list.isEmpty()) {
            throw new ApplicationException(ErrorCode.CONTENT_NOT_FOUND, "등록된 특강이 없습니다.");
        }
        return list;
    }
}
