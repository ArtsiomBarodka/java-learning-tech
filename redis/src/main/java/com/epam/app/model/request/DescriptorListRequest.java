package com.epam.app.model.request;

import com.epam.app.validation.OptionalNotBlank;
import lombok.Data;

import java.util.List;

@Data
public class DescriptorListRequest {
    private List<DescriptorRequest> descriptorRequests;

    @Data
    public static class DescriptorRequest {
        private String accountId;

        private String clientIp;

        @OptionalNotBlank
        private String requestType;
    }
}
