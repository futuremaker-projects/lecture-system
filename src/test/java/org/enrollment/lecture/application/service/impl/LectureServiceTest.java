package org.enrollment.lecture.application.service.impl;

import org.enrollment.lecture.controller.dto.enrollment.EnrollmentRequestDto;
import org.enrollment.lecture.controller.dto.enrollment.EnrollmentResponseDto;
import org.enrollment.lecture.controller.dto.lectureinfo.LectureInfoResponseDto;
import org.enrollment.lecture.domain.entity.*;
import org.enrollment.lecture.domain.repository.EnrollmentHistoryRepository;
import org.enrollment.lecture.domain.service.LectureService;
import org.enrollment.lecture.controller.dto.lecture.LectureResponseDto;
import org.enrollment.lecture.domain.repository.EnrollmentRepository;
import org.enrollment.lecture.domain.repository.LectureRepository;
import org.enrollment.lecture.domain.repository.UserRepository;
import org.enrollment.lecture.infra.exception.ApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @InjectMocks LectureService sut;

    @Mock LectureRepository lectureRepository;
    @Mock UserRepository userRepository;
    @Mock EnrollmentRepository enrollmentRepository;
    @Mock EnrollmentHistoryRepository enrollmentHistoryRepository;

    @DisplayName("강의 id와 사용자 id로 특강을 수강한다")
    @Test
    void givenLectureIdAndUserId_whenSavingEnrollment_thenNothingReturn() {
        // given
        long lectureId = 1L;
        long userId = 1L;
        int userLimit = 5;
        long enrollmentId = 1L;

        // 강의 조회
        Lecture lecture = Lecture.of(lectureId, userLimit);
        given(lectureRepository.findById(lectureId)).willReturn(lecture);
        // 학생 조회
        User user = User.of(userId);
        given(userRepository.findById(userId)).willReturn(user);
        // 해당 강의에 학생을 등록
        Enrollment enrollment = Enrollment.of(enrollmentId, lectureId, userId);
        given(enrollmentRepository.save(any(Enrollment.class))).willReturn(enrollment);
        // 특강을 특록후 특강 등록 히스토리에 저장
        EnrollmentHistory enrollmentHistory = EnrollmentHistory.of(enrollment);
        given(enrollmentHistoryRepository.save(any(EnrollmentHistory.class))).willReturn(enrollmentHistory);

        // when
        sut.enrollLecture(EnrollmentRequestDto.of(lectureId, userId));

        // then
        then(lectureRepository).should().findById(lectureId);
        then(userRepository).should().findById(userId);
        then(enrollmentHistoryRepository).should().save(any(EnrollmentHistory.class));

        ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        then(enrollmentRepository).should().save(enrollmentCaptor.capture());
        Enrollment capturedEnrollment = enrollmentCaptor.getValue();

        assertThat(capturedEnrollment.getLectureId()).isEqualTo(lectureId);
        assertThat(capturedEnrollment.getUserId()).isEqualTo(userId);
    }

    @DisplayName("특강신청시 중복된 학생이 신청한다면 예외를 반환한다.")
    @Test
    void given_when_then() {
        // given
        long lectureId = 1L;
        long userId = 1L;
        Lecture lecture = Lecture.of(lectureId);
        User user = User.of(userId);
        List<Enrollment> enrollments = enrollments();

        // 특강 조회
        given(lectureRepository.findById(lectureId)).willReturn(lecture);
        // 유저 조회
        given(userRepository.findById(userId)).willReturn(user);
        // 특강 id를 이용하여 특강에 등록한 학생목록 조회
        given(enrollmentRepository.findAllByLectureId(lectureId)).willReturn(enrollments);

        // when
        Throwable t = catchThrowable(() -> sut.enrollLecture(EnrollmentRequestDto.of(lectureId, userId)));

        // then
        assertThat(t)
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("이미 강의를 신청한 유저입니다. id - %d".formatted(userId));
    }

    @DisplayName("특강신청시 등록할 수 있는 학생수가 초과되면 예외를 반환한다")
    @Test
    void givenLectureIdAndUserId_whenIfLectureUserLimitExceeded_thenThrowApplicationException() {
        // given
        long lectureId = 1L;
        long userId = 6L;
        int userLimit = 5;
        EnrollmentRequestDto requestDto = EnrollmentRequestDto.of(lectureId, userId);
        Lecture lecture = Lecture.of(lectureId, userLimit);
        User user = User.of(userId);
        List<Enrollment> enrollments = enrollments();

        given(lectureRepository.findById(lectureId)).willReturn(lecture);
        given(userRepository.findById(userId)).willReturn(user);
        given(enrollmentRepository.findAllByLectureId(lectureId)).willReturn(enrollments);

        // when
        Throwable t = catchThrowable(() -> sut.enrollLecture(requestDto));

        // then
        assertThat(t)
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("등록할 수 있는 학생수를 초과하였습니다.");
    }

    @DisplayName("모든 특강을 검색하면 특강 목록을 반환한다.")
    @Test
    void givenNothing_whenRequestingLectures_thenReturnLectures() {
        // given
        LocalDateTime lecture1OpenedAt = LocalDateTime.now();
        LocalDateTime lecture2OpenedAt = LocalDateTime.now();
        List<Lecture> list = List.of(
                Lecture.of(1L, 30, LectureInfo.of(1L, "lecture1", lecture1OpenedAt)),
                Lecture.of(2L, 40, LectureInfo.of(2L, "lecture2", lecture2OpenedAt))
        );
        given(lectureRepository.findAll()).willReturn(list);
        List<LectureResponseDto> lectureDtos = list.stream().map(LectureResponseDto::from).toList();

        // when
        List<LectureResponseDto> lectures = sut.selectLectures();

        // then
        assertThat(lectures).isNotNull();
        assertThat(lectures.get(0).openedAt()).isEqualTo(lectureDtos.get(0).openedAt());
        assertThat(lectures.size()).isEqualTo(lectureDtos.size());
        assertThat(lectures)
                .extracting("id", "userLimit", "lectureInfoDto")
                .containsExactlyInAnyOrder(
                        tuple(1L, 30, LectureInfoResponseDto.of(1L, "lecture1", lecture1OpenedAt)),
                        tuple(2L, 40, LectureInfoResponseDto.of(2L, "lecture2", lecture2OpenedAt))
                );

        then(lectureRepository).should().findAll();
    }

    @DisplayName("특강을 조회하면 빈 리스트를 반환한다.")
    @Test
    void givenNothing_whenRequestingLectures_thenReturnsEmptyList() {
        // given
        given(lectureRepository.findAll()).willReturn(List.of());

        // when
        List<LectureResponseDto> lectures = sut.selectLectures();

        // then
        assertThat(lectures).isNotNull();
        assertThat(lectures.size()).isEqualTo(0);

        then(lectureRepository).should().findAll();
    }

    @DisplayName("유저가 신청자 목록에 있다면 목록을 반환한다.")
    @Test
    void givenUserId_whenVerifyIfUserOnLectureList_thenTrue() {
        // given
        long userId = 1L;

        given(enrollmentRepository.findAllByUserId(userId)).willReturn(enrollments());

        // when
        List<EnrollmentResponseDto> responseDtos = sut.hasUserIdOnLectureUserList(userId);

        // then
        assertThat(responseDtos.size()).isEqualTo(5);
    }

    @DisplayName("유저가 신청자 목록에 없다면 예외를 던진다.")
    @Test
    void givenUserId_whenVerifyIfUserNotOnLectureList_thenThrowException() {
        // given
        long userId = 1L;
        given(enrollmentRepository.findAllByUserId(userId)).willReturn(List.of());

        // when
        Throwable t = catchThrowable(() -> sut.hasUserIdOnLectureUserList(userId));

        // then
        assertThat(t)
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("등록된 특강이 없습니다.");
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