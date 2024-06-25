package org.enrollment.lecture.infra.repository.userAccount;

import org.enrollment.lecture.domain.entity.UserAccount;

public interface UserAccountRepository {

    UserAccount findById(Long id);

}
