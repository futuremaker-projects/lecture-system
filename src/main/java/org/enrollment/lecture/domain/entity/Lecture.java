package org.enrollment.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '강의명'")
    private String name;

    @Comment("수강가능한 사람수")
    @Column(nullable = false)
    private int limitUser;

    @Comment("개강일")
    private LocalDate openedAt;

    public static Lecture of(long id, String name, int limitUser, LocalDate openedAt) {
        return new Lecture(id, name, limitUser, openedAt);
    }

    @PrePersist
    public void openedAt() {
        this.openedAt = LocalDate.now();
    }

    public Lecture(Long id) {
        this.id = id;
    }

    public static Lecture of(long lectureId) {
        return new Lecture(lectureId);
    }
}
