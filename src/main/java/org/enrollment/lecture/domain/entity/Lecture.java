package org.enrollment.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.enrollment.lecture.infra.exception.ApplicationException;
import org.enrollment.lecture.infra.exception.ErrorCode;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("수강가능한 사람수")
    @Column(nullable = false)
    private Integer userLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_info_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private LectureInfo lectureInfo;

    private LocalDateTime openedAt;

    @PrePersist
    public void openedAt() {
        this.openedAt = LocalDateTime.now();
    }

    public Lecture(Long id) {
        this.id = id;
    }

    public Lecture(Long id, Integer userLimit) {
        this.id = id;
        this.userLimit = userLimit;
    }

    public static Lecture of(long id) {
        return new Lecture(id);
    }

    public static Lecture of(long id, Integer userLimit) {
        return new Lecture(id, userLimit);
    }

    public static Lecture of(long id, Integer userLimit, LectureInfo lectureInfo) {
        return new Lecture(id, userLimit, lectureInfo, LocalDateTime.now());
    }

        public void checkIfExceededUserLimit(List<Enrollment> enrolledLectures) {
            if (this.userLimit <= enrolledLectures.size()) {
                throw new ApplicationException(ErrorCode.CONFLICT, "등록할 수 있는 학생수를 초과하였습니다. 수강가능 학생수 - %d".formatted(this.userLimit));
            }
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecture lecture)) return false;
        return id != null && id.equals(lecture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
