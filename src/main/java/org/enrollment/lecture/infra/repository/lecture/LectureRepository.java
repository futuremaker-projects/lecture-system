package org.enrollment.lecture.infra.repository.lecture;

import org.enrollment.lecture.domain.entity.Lecture;

import java.util.List;

public interface LectureRepository {

    List<Lecture> findAll();
    Lecture findById(Long id);

}
