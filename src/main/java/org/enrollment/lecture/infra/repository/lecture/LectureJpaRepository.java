package org.enrollment.lecture.infra.repository.lecture;

import org.enrollment.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {
}
