package org.enrollment.lecture.infra.repository.lecture;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.enrollment.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LectureJpaRepository extends JpaRepository<Lecture, Long> {

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @QueryHints({
                @QueryHint(name = "jakarta.persistence.lock.timeout", value = "1500")
        })
        @Query("SELECT l FROM Lecture l WHERE l.id = :id")
        Optional<Lecture> findByIdWithLock(@Param("id") Long id);

}
