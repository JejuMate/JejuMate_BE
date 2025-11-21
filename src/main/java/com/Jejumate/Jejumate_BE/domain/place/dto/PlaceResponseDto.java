package com.Jejumate.Jejumate_BE.domain.place.dto;

import com.Jejumate.Jejumate_BE.domain.place.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {
    private Long id;
    private String name;
    private String category;
    private String address;
    private String imageUrl;
    private String description;

    public static PlaceResponseDto from(Place place) {
        return PlaceResponseDto.builder()
                .id(place.getId())
                .name(place.getName())
                .category(place.getCategory())
                .address(place.getAddress())
                .imageUrl(place.getImageUrl())
                .description(place.getDescription())
                .build();
    }
}
