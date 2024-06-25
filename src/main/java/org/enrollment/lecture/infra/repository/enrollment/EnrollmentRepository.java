package org.enrollment.lecture.infra.repository.enrollment;

import org.enrollment.lecture.domain.entity.Enrollment;

import java.util.List;
import java.util.Optional;

/**
 *  JPA에서 MyBatis 등 다른 Persist Api로 자유로운 변경을 위해 인터페이스로 추상화 시킴
 */
public interface EnrollmentRepository {

    Enrollment findById(Long id);

    List<Enrollment> findAllByLectureId(Long lectureId);

    Enrollment findByUserId(Long userId);

    Enrollment save(Enrollment enrollment);

}
