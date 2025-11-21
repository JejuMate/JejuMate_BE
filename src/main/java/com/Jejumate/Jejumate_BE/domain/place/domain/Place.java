package com.Jejumate.Jejumate_BE.domain.place.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

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

    @Column(nullable = false)
    private Long visitCount = 0L;

    @Builder
    public Place(String name, String category, String address, String imageUrl, String apiId, Long visitCount) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.imageUrl = imageUrl;
        this.apiId = apiId;
        this.visitCount = visitCount != null ? visitCount : 0L;
    }

    public void increaseVisitCount() {
        this.visitCount++;
    }
}