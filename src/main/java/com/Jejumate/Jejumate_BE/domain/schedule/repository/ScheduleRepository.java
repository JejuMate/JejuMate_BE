package com.Jejumate.Jejumate_BE.domain.schedule.repository;

import com.Jejumate.Jejumate_BE.domain.schedule.domain.Schedule;
import com.Jejumate.Jejumate_BE.domain.schedule.enums.ScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 목록 조회
    @Query("SELECT s FROM Schedule s " +
            "WHERE s.userId = :userId " +
            "AND s.status != 'DELETED' " +
            "ORDER BY s.createdAt DESC")
    List<Schedule> findSchedulesByUserId(@Param("userId") Long userId);

    // 상세 조회
    @Query("SELECT s FROM Schedule s " +
            "LEFT JOIN FETCH s.items " +
            "WHERE s.id = :scheduleId " +
            "AND s.userId = :userId")
    Optional<Schedule> findScheduleDetailById(
            @Param("scheduleId") Long scheduleId,
            @Param("userId") Long userId
    );
}
