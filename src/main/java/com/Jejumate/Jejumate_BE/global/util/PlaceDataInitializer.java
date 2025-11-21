package com.Jejumate.Jejumate_BE.global.util;

import com.Jejumate.Jejumate_BE.domain.place.domain.Place;
import com.Jejumate.Jejumate_BE.domain.place.dto.VisitJejuDto;
import com.Jejumate.Jejumate_BE.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceDataInitializer implements ApplicationRunner {

    private final PlaceRepository placeRepository;
    @Value("${visit-jeju.api-key}")
    private String API_KEY;

    @Override
    public void run(ApplicationArguments args) {

        if (placeRepository.count() > 0) {
            System.out.println(" [Init] DB에 데이터가 이미 존재합니다 (" + placeRepository.count() + "개).");
            return;
        }

        System.out.println(" [Init] VisitJeju 데이터 수집 시작");

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("http://api.visitjeju.net/vsjApi/contents")
                .build();

        int page = 1;

        while (true) {
            int currentPage = page;

            // 20페이지 넘어가면 강제 종료
            if (currentPage > 20) {
                break;
            }

            try {
                System.out.print("📡 " + currentPage + " 페이지 요청 중... ");

                VisitJejuDto response = webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/searchList")
                                .queryParam("apiKey", API_KEY)
                                .queryParam("locale", "kr")
                                .queryParam("category", "c1")
                                .queryParam("page", currentPage)
                                .queryParam("pageSize", 100)
                                .build())
                        .retrieve()
                        .bodyToMono(VisitJejuDto.class)
                        .block();

                if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
                    System.out.println("\n [End] 수집 종료!");
                    break;
                }

                List<Place> placeList = new ArrayList<>();

                for (VisitJejuDto.Item item : response.getItems()) {
                    // 1. 이미지
                    String imageUrl = null;
                    if (item.getRepPhoto() != null && item.getRepPhoto().getPhotoId() != null) {
                        imageUrl = item.getRepPhoto().getPhotoId().getImgPath();
                    }

                    // 2. 주소
                    String finalAddress = item.getRoadAddress();
                    if (finalAddress == null || finalAddress.isEmpty()) {
                        finalAddress = item.getAddress();
                    }

                    // 3. 카테고리
                    String category = "기타";
                    if (item.getContentsCd() != null) {
                        category = item.getContentsCd().getLabel();
                    }

                    // ✨ 4. 설명(description) - null이면 빈 문자열 처리
                    String description = item.getIntroduction();
                    if (description == null) {
                        description = "";
                    }

                    Place place = Place.builder()
                            .apiId(item.getContentsId())
                            .name(item.getTitle())
                            .category(category)
                            .address(finalAddress)
                            .imageUrl(imageUrl)
                            .description(description)
                            .visitCount(0L)
                            .build();

                    placeList.add(place);
                }

                placeRepository.saveAll(placeList);
                System.out.println(" 저장 완료 (" + placeList.size() + "개)");

                page++;
                Thread.sleep(500);

            } catch (Exception e) {
                System.err.println("\n [Error] " + currentPage + " 페이지 에러: " + e.getMessage());
                page++;
            }
        }
        System.out.println(" [Init] 완료!");
    }
}