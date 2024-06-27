package org.enrollment.lecture.application.service;

import org.enrollment.lecture.controller.dto.enrollment.EnrollmentRequestDto;
import org.enrollment.lecture.domain.entity.Enrollment;
import org.enrollment.lecture.domain.repository.EnrollmentRepository;
import org.enrollment.lecture.domain.service.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LectureEnrollConcurrencyTest {

    @Autowired
    private LectureService lectureService;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void given_when_then() throws InterruptedException {
        // given
        long lectureId = 1L;

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
               try {
                   lectureService.enrollLecture(EnrollmentRequestDto.of(lectureId, userId));
               } finally {
                   latch.countDown();
               }
            });
        }
        latch.await();
        executorService.shutdown();

        // when
        List<Enrollment> enrollments = enrollmentRepository.findAllByLectureId(lectureId);

        // then
        assertThat(enrollments.size()).isEqualTo(30);
    }

}
