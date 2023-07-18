package com.epam.app.rest;

import com.epam.app.facade.RateLimitFacade;
import com.epam.app.model.request.DescriptorListRequest;
import com.epam.app.model.response.RateLimitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rate-limit")
@RequiredArgsConstructor
public class RateLimitController {
    private final RateLimitFacade rateLimitFacade;

    @PostMapping
    public ResponseEntity<RateLimitResponse> processRateLimitRequest(@RequestBody DescriptorListRequest descriptorListRequest) {
        boolean limitExceeded = rateLimitFacade.isLimitExceeded(descriptorListRequest);
        if (limitExceeded) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(toRateLimitResponse(descriptorListRequest));
        }
        return ResponseEntity.ok(toRateLimitResponse(descriptorListRequest));
    }

    private RateLimitResponse toRateLimitResponse(DescriptorListRequest source) {
        var rateLimitResponse = new RateLimitResponse();
        rateLimitResponse.setDescriptor(source.getDescriptorRequests()
                .stream()
                .map(s -> {
                    var description = new RateLimitResponse.RateLimitDescription();
                    description.setAccountId(s.getAccountId());
                    description.setClientIp(s.getClientIp());
                    description.setRequestType(s.getRequestType());
                    return description;
                })
                .toList());

        return rateLimitResponse;
    }
}
