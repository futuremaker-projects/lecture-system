package org.enrollment.lecture.domain.repository;

import org.enrollment.lecture.domain.entity.Lecture;

import java.util.List;

public interface LectureRepository {

    List<Lecture> findAll();
    Lecture findById(Long id);

}
