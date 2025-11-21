package com.Jejumate.Jejumate_BE.domain.place.controller;

import com.Jejumate.Jejumate_BE.domain.place.domain.Place;
import com.Jejumate.Jejumate_BE.domain.place.dto.PlaceResponseDto;
import com.Jejumate.Jejumate_BE.domain.place.repository.PlaceRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepository placeRepository;

    @GetMapping("/popular")
    @Operation(
            summary = "인기 여행지 top 7 반환",
            description = "제주도 방문자수 top 7 여행지를 반환합니다."
    )
    public ResponseEntity<List<PlaceResponseDto>> getPopularPlaces() {

        List<Place> placeList = placeRepository.findTop7ByOrderByVisitCountDesc();

        List<PlaceResponseDto> responseDtoList = placeList.stream()
                .map(PlaceResponseDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }
}
