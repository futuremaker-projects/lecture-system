package org.enrollment.lecture.infra.repository.enrollment.impl;

import org.enrollment.lecture.infra.repository.enrollment.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnrollmentRepositoryImplTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void givenUserIdAsNull_whenRequestingVerification_whenThrowException() {
        // given
        Long userId = null;

        // when
        Throwable t = catchThrowable(() -> enrollmentRepository.findByUserId(userId));

        // then
        assertThat(t).isNotNull();
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class);


    }
}