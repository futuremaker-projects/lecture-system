package org.enrollment.lecture.application.service.impl;

import org.enrollment.lecture.application.service.LectureService;
import org.enrollment.lecture.domain.dto.lecture.LectureRequestDto;
import org.enrollment.lecture.domain.dto.lecture.LectureResponseDto;
import org.enrollment.lecture.domain.entity.Enrollment;
import org.enrollment.lecture.domain.entity.Lecture;
import org.enrollment.lecture.domain.entity.UserAccount;
import org.enrollment.lecture.infra.repository.enrollment.EnrollmentRepository;
import org.enrollment.lecture.infra.repository.lecture.LectureRepository;
import org.enrollment.lecture.infra.repository.userAccount.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @InjectMocks
    LectureService sut;

    @Mock
    LectureRepository lectureRepository;
    @Mock
    UserAccountRepository userAccountRepository;
    @Mock
    EnrollmentRepository enrollmentRepository;

    @DisplayName("강의 id와 사용자 id로 특강을 수강한다")
    @Test
    void givenLectureIdAndUserId_whenSavingEnrollment_thenNothingReturn() {
        // given
        long lectureId = 1L;
        long userId = 1L;
        long enrollmentId = 1L;
        LectureRequestDto requestDto = LectureRequestDto.of(lectureId, userId);
        Lecture lecture = Lecture.of(lectureId);
        UserAccount userAccount = UserAccount.of(userId);
        Enrollment enrollment = Enrollment.of(enrollmentId, lectureId, userId);

        given(lectureRepository.findById(lectureId)).willReturn(lecture);
        given(userAccountRepository.findById(userId)).willReturn(userAccount);
        given(enrollmentRepository.save(any(Enrollment.class))).willReturn(enrollment);

        // when
        sut.applyLecture(requestDto);

        // then
        then(lectureRepository).should().findById(lectureId);
        then(userAccountRepository).should().findById(userId);

        ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        then(enrollmentRepository).should().save(enrollmentCaptor.capture());
        Enrollment capturedEnrollment = enrollmentCaptor.getValue();

        assertThat(capturedEnrollment.getLectureId()).isEqualTo(lectureId);
        assertThat(capturedEnrollment.getUserId()).isEqualTo(userId);
    }

    @Test
    void 특강신청시_등록할_수_있는_학생수가_초과되면_예외를_반환한다() {
        // given
        long lectureId = 1L;
        long userId = 1L;
        LectureRequestDto requestDto = LectureRequestDto.of(lectureId, userId);
        Lecture lecture = Lecture.of(lectureId);
        UserAccount userAccount = UserAccount.of(userId);
        given(lectureRepository.findById(lectureId)).willReturn(lecture);
        given(userAccountRepository.findById(userId)).willReturn(userAccount);

        // when
        Throwable t = catchThrowable(() -> sut.applyLecture(requestDto));

        // then
        assertThat(t)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("등록할 수 있는 학생수를 초과하였습니다.");
    }

    @DisplayName("모든 특강을 검색하면 특강 목록을 반환한다.")
    @Test
    void givenNothing_whenRequestingLectures_thenReturnLectures() {
        // given
        List<Lecture> list = List.of(
                Lecture.of(1L, "special", 30, LocalDate.now()),
                Lecture.of(2L, "special2", 40, LocalDate.now())
        );
        given(lectureRepository.findAll()).willReturn(list);

        // when
        List<LectureResponseDto> lectures = sut.selectLectures();

        // then
        assertThat(lectures).isNotNull();
        assertThat(lectures.size()).isEqualTo(2);
        assertThat(lectures)
                .extracting("id", "name", "limitUser")
                .containsExactlyInAnyOrder(
                        tuple(1L, "special", 30),
                        tuple(2L, "special2", 40)
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

    @DisplayName("유저아이디가 신청자 목록에 있다면 `true`를 리턴한다.")
    @Test
    void givenUserId_whenVerifyIfUserOnLectureList_thenTrue() {
        // given
        long lectureId = 1L;
        long userId = 1L;
        Enrollment enrollment = Enrollment.of(lectureId, userId);

        given(enrollmentRepository.findByUserId(userId)).willReturn(enrollment);

        // when
        boolean result = sut.hasUserIdOnLectureUserList(userId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("유저아이디가 신청자 목록에 없다면 `false`를 리턴한다.")
    @Test
    void givenUserId_whenVerifyIfUserNotOnLectureList_thenFalse() {
        // given
        long userId = 1L;
        given(enrollmentRepository.findByUserId(userId)).willReturn(null);

        // when
        boolean result = sut.hasUserIdOnLectureUserList(userId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void givenUserIdAsNull_whenRequestingVerification_whenThrowException() {
        // given
        Long userId = null;
        given(enrollmentRepository.findByUserId(userId)).willReturn(null);

        // when
        Throwable t = catchThrowable(() -> sut.hasUserIdOnLectureUserList(userId));

        // then
        assertThat(t).isNotNull();
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class);


    }

}