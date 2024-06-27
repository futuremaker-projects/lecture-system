package org.enrollment.lecture.infra.repository.user;

import org.enrollment.lecture.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
