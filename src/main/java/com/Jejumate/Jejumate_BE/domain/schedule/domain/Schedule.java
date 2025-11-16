package com.Jejumate.Jejumate_BE.domain.schedule.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @Column(name = "travel_style", length = 100)
    private String travelStyle;

    @Column(length = 50)
    private String companions;

    @Column(name = "age_group", length = 20)
    private String ageGroup;

    @Column(name = "additional_request", columnDefinition = "TEXT")
    private String additionalRequest;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ScheduleStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "schedule_id", nullable = false)
    private List<ScheduleItem> items = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Schedule(
            Long userId,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            Integer totalDays,
            String travelStyle,
            String companions,
            String ageGroup,
            String additionalRequest,
            ScheduleStatus status
    ) {
        this.userId = userId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.travelStyle = travelStyle;
        this.companions = companions;
        this.ageGroup = ageGroup;
        this.additionalRequest = additionalRequest;
        this.status = status != null ? status : ScheduleStatus.DRAFT;
        this.items = new ArrayList<>();
    }

    public void addItem(ScheduleItem item) {
        this.items.add(item);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateStatus(ScheduleStatus status) {
        this.status = status;
    }

    public enum ScheduleStatus {
        DRAFT, SAVED, DELETED
    }
}
