package com.epam.app.model.response;

import com.epam.app.model.request.DescriptorListRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class RateLimitResponse {
    private List<RateLimitDescription> descriptor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RateLimitDescription extends DescriptorListRequest.DescriptorRequest {
    }
}
