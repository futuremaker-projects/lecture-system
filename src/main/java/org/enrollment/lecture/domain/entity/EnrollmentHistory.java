package org.enrollment.lecture.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EnrollmentHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Enrollment enrollment;

    private LocalDateTime createdAt;

    @PrePersist
    void createdAt() {
        this.createdAt = LocalDateTime.now();
    }

    public EnrollmentHistory(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public static EnrollmentHistory of(Enrollment enrollment) {
        return new EnrollmentHistory(enrollment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollmentHistory enrollmentHistory)) return false;
        return id != null && id.equals(enrollmentHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
