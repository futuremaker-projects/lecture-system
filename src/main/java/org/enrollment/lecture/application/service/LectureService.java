package org.enrollment.lecture.application.service;

import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.domain.dto.lecture.LectureRequestDto;
import org.enrollment.lecture.domain.dto.lecture.LectureResponseDto;
import org.enrollment.lecture.domain.entity.Enrollment;
import org.enrollment.lecture.domain.entity.Lecture;
import org.enrollment.lecture.domain.entity.UserAccount;
import org.enrollment.lecture.infra.exception.ApplicationException;
import org.enrollment.lecture.infra.exception.ErrorCode;
import org.enrollment.lecture.infra.repository.enrollment.EnrollmentRepository;
import org.enrollment.lecture.infra.repository.lecture.LectureRepository;
import org.enrollment.lecture.infra.repository.userAccount.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserAccountRepository userAccountRepository;
    private final EnrollmentRepository enrollmentRepository;

    public void applyLecture(LectureRequestDto requestDto) {
        Lecture lecture = lectureRepository.findById(requestDto.lectureId());
        UserAccount userAccount = userAccountRepository.findById(requestDto.userId());
        List<Enrollment> enrolledLectures = enrollmentRepository.findAllByLectureId(requestDto.lectureId());
        List<Long> enrolledUserIds = enrolledLectures.stream().map(Enrollment::getUserId).toList();
        if (enrolledUserIds.contains(requestDto.userId())){
            throw new ApplicationException(ErrorCode.USER_EXISTED, "이미 강의를 신청한 유저입니다.");
        }
        if (lecture.getLimitUser() <= enrolledLectures.size()) {
            throw new ApplicationException(ErrorCode.CONFLICT, "등록할 수 있는 학생수를 초과하였습니다.");
        }
        enrollmentRepository.save(Enrollment.of(lecture.getId(), userAccount.getId()));
    }

    @Transactional(readOnly = true)
    public List<LectureResponseDto> selectLectures() {
        return lectureRepository.findAll().stream()
                .map(LectureResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean hasUserIdOnLectureUserList(Long userId) {
        Enrollment enrollment = enrollmentRepository.findByUserId(userId);
        return enrollment != null;
    }
}
