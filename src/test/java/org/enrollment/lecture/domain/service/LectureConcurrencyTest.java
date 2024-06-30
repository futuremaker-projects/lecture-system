package org.enrollment.lecture.domain.service;

import org.enrollment.lecture.controller.dto.enrollment.EnrollmentRequestDto;
import org.enrollment.lecture.domain.entity.Enrollment;
import org.enrollment.lecture.domain.repository.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LectureConcurrencyTest {

    @Autowired
    private LectureService lectureService;
    @Autowired
    private EnrollmentRepository enrollmentRepository;

    /**
     * 100명의 사용자가 임의로 30개의 Thread 를 사용하여 특강 등록 시도시
     *  제한된 30명의 사용자만 특강을 신청할 수 있는지 테스트함
     */
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

