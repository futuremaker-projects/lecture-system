package org.enrollment.lecture.domain.repository;

import org.enrollment.lecture.domain.entity.EnrollmentHistory;

public interface EnrollmentHistoryRepository {

    EnrollmentHistory save(EnrollmentHistory enrollmentHistory);

}
