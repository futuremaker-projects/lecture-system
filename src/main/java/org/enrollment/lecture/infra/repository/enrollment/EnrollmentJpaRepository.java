package org.enrollment.lecture.infra.repository.enrollment;

import org.enrollment.lecture.domain.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentJpaRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByUserId(Long userId);

    List<Enrollment> findAllByLectureId(Long lectureId);
    List<Enrollment> findAllByUserId(Long userId);

}
