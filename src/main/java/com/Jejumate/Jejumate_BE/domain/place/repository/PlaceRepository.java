package com.Jejumate.Jejumate_BE.domain.place.repository;

import com.Jejumate.Jejumate_BE.domain.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    boolean existsByName(String name);

    Optional<Place> findByName(String name);

    List<Place> findTop7ByOrderByVisitCountDesc();
}
