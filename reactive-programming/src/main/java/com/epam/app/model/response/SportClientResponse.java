package com.epam.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SportClientResponse {
    private List<DataItem> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataItem{
        private Integer id;
        private Attributes attributes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attributes{
        private String name;
    }
}
