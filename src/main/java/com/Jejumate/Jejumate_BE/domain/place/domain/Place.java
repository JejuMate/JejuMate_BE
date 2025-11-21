package com.Jejumate.Jejumate_BE.domain.place.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String apiId;

    @Column(nullable = false)
    private String name;

    private String category;
    private String address;

    @Column(length = 1000)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long visitCount = 0L;

    @Builder
    public Place(String name, String category, String address, String imageUrl, String apiId, Long visitCount, String description) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.imageUrl = imageUrl;
        this.apiId = apiId;
        this.visitCount = visitCount != null ? visitCount : 0L;
        this.description = description;
    }

    public void increaseVisitCount() {
        this.visitCount++;
    }
}
