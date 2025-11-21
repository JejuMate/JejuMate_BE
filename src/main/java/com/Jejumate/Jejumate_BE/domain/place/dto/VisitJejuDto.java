package com.Jejumate.Jejumate_BE.domain.place.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitJejuDto {

    private String result;
    private String resultMessage;
    private int totalCount;
    private int resultCount;
    private List<Item> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        @JsonProperty("contentsid")
        private String contentsId;

        @JsonProperty("title")
        private String title;

        @JsonProperty("address")
        private String address;

        @JsonProperty("roadaddress")
        private String roadAddress;

        @JsonProperty("contentscd")
        private ContentsCd contentsCd;

        @JsonProperty("repPhoto")
        private RepPhoto repPhoto;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentsCd {
        private String label;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RepPhoto {
        @JsonProperty("photoid")
        private PhotoId photoId;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhotoId {
        @JsonProperty("imgpath")
        private String imgPath;

        @JsonProperty("thumbnailpath")
        private String thumbnailPath;
    }
}
