package com.epam.app.model.response;

import lombok.Data;

import java.util.List;

@Data
public class SportClientResponse {
    private List<DataItem> data;

    @Data
    public static class DataItem {
        private Long id;
        private Attributes attributes;
    }

    @Data
    public static class Attributes {
        private String name;
    }
}
