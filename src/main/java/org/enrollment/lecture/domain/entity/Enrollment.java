package org.enrollment.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Enrollment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lectureId;

    @Column(nullable = false)
    private Long userId;

    @Comment("수강일")
    private LocalDateTime enrolledAt;

    public Enrollment(long lectureId, long userId) {
        this.lectureId = lectureId;
        this.userId = userId;
    }

    public Enrollment(Long id, long lectureId, long userId) {
        this.id = id;
        this.lectureId = lectureId;
        this.userId = userId;
    }

    public static Enrollment of(long lectureId, long userId) {
        return new Enrollment(lectureId, userId);
    }

    public static Enrollment of(Long id, long lectureId, long userId) {
        return new Enrollment(id, lectureId, userId);
    }

    @PrePersist
    void enrolledAt() {
        enrolledAt = LocalDateTime.now();
    }

}
