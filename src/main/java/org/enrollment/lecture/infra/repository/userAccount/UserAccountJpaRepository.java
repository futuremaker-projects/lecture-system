package org.enrollment.lecture.infra.repository.userAccount;

import org.enrollment.lecture.domain.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountJpaRepository extends JpaRepository<UserAccount, Long> {
}
