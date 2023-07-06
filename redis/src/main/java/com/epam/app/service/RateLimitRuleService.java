package com.epam.app.service;

import com.epam.app.model.dto.RateLimitRule;
import com.epam.app.model.request.DescriptorListRequest;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface RateLimitRuleService {
    @NonNull Optional<RateLimitRule> findMatchingRule(@NonNull DescriptorListRequest.DescriptorRequest descriptor);
}
