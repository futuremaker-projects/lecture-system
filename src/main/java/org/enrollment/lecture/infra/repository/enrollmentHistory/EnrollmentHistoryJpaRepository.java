package org.enrollment.lecture.infra.repository.enrollmentHistory;

import org.enrollment.lecture.domain.entity.EnrollmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentHistoryJpaRepository extends JpaRepository<EnrollmentHistory, Long> {
}
