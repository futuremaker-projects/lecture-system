package org.enrollment.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.enrollment.lecture.infra.exception.ApplicationException;
import org.enrollment.lecture.infra.exception.ErrorCode;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    public void checkIfExistedUserId(List<Enrollment> enrolledLectures, Long userId) {
        List<Long> enrolledUserIds = enrolledLectures.stream().map(Enrollment::getUserId).toList();
        if (enrolledUserIds.contains(userId)){
            throw new ApplicationException(ErrorCode.USER_EXISTED, "이미 강의를 신청한 유저입니다. id - %d".formatted(userId));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment enrollment)) return false;
        return id != null && id.equals(enrollment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
