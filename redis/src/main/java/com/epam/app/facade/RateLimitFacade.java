package com.epam.app.facade;

import com.epam.app.model.request.DescriptorListRequest;
import com.epam.app.service.RateLimitDataService;
import com.epam.app.service.RateLimitRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RateLimitFacade {
    private final RateLimitDataService dataService;
    private final RateLimitRuleService ruleService;

    public boolean isLimitExceeded(@NonNull DescriptorListRequest descriptorList) {
        for (var descriptor : descriptorList.getDescriptorRequests()) {
            var rule = ruleService.findMatchingRule(descriptor);
            if (rule.isPresent()) {
                Long currentRequestCount = dataService.increaseRequestCount(descriptor, rule.get().getTimeInterval());
                if (currentRequestCount > rule.get().getAllowedNumberOfRequests()) {
                    return true;
                }
            }
        }

        return false;
    }
}
