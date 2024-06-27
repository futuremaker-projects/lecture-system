package org.enrollment.lecture.infra.repository.lecture.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.enrollment.lecture.domain.entity.Lecture;
import org.enrollment.lecture.infra.exception.ApplicationException;
import org.enrollment.lecture.infra.exception.ErrorCode;
import org.enrollment.lecture.infra.repository.lecture.LectureJpaRepository;
import org.enrollment.lecture.infra.repository.lecture.LectureRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll();
    }

    @Override
    public Lecture findById(Long id) {
        return lectureJpaRepository.findById(id).orElseThrow(
                () -> new ApplicationException(ErrorCode.CONTENT_NOT_FOUND, "enrollment not found : id - %d".formatted(id)));
    }
}
