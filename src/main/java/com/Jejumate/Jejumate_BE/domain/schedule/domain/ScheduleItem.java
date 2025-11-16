package com.Jejumate.Jejumate_BE.domain.schedule.domain;

import com.Jejumate.Jejumate_BE.domain.schedule.enums.TimeSlot;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot", nullable = false, length = 20)
    private TimeSlot timeSlot;

    @Column(name = "place_name", nullable = false)
    private String placeName;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(length = 500)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public ScheduleItem(
            Integer dayNumber,
            TimeSlot timeSlot,
            String placeName,
            String category,
            BigDecimal latitude,
            BigDecimal longitude,
            String address,
            String description,
            Integer orderIndex
    ) {
        this.dayNumber = dayNumber;
        this.timeSlot = timeSlot;
        this.placeName = placeName;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
        this.orderIndex = orderIndex != null ? orderIndex : 0;
    }
}