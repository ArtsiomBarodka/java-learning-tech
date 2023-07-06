package com.epam.app.service;

import com.epam.app.config.RulesConfig;
import com.epam.app.model.dto.RateLimitRule;
import com.epam.app.model.request.DescriptorListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateLimitRuleServiceImpl implements RateLimitRuleService {
    private final RulesConfig rulesConfig;

    @Override
    public @NonNull Optional<RateLimitRule> findMatchingRule(@NonNull DescriptorListRequest.DescriptorRequest descriptor) {
        for (var rule : rulesConfig.getRules()) {
            if (matchesRule(rule, descriptor)) {
                return Optional.of(rule);
            }
        }
        return Optional.empty();
    }

    private boolean matchesRule(RateLimitRule rule, DescriptorListRequest.DescriptorRequest descriptor) {
        boolean matchesAccountId = rule.getAccountId() == null ||
                rule.getAccountId().isBlank() ||
                (descriptor.getAccountId() != null && descriptor.getAccountId().equals(rule.getAccountId()));

        boolean matchesClientIp = rule.getClientIp() == null ||
                (descriptor.getClientIp() != null && descriptor.getClientIp().equals(rule.getClientIp()));

        boolean matchesRequestType = rule.getRequestType() == null ||
                (descriptor.getRequestType() != null && descriptor.getRequestType().equals(rule.getRequestType()));

        return matchesAccountId && matchesClientIp && matchesRequestType;
    }

}
