package org.enrollment.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("강의명")
    @Column(columnDefinition = "VARCHAR(50) NOT NULL COMMENT '강의명'")
    private String name;

    private LocalDateTime createdAt;

    @PrePersist
    void openedAt() {
        createdAt = LocalDateTime.now();
    }

    public LectureInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static LectureInfo of(Long id, String name, LocalDateTime openedAt) {
        return new LectureInfo(id, name, openedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LectureInfo lectureInfo)) return false;
        return id != null && id.equals(lectureInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
