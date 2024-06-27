package org.enrollment.lecture.domain.repository;

import org.enrollment.lecture.domain.entity.User;

public interface UserRepository {

    User findById(Long id);

}
