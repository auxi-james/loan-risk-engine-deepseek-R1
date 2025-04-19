package com.loanrisk;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RuleLoaderService {
    private final ScoringRuleRepository ruleRepository;

    @Cacheable("rules")
    public List<ScoringRule> loadActiveRules() {
        List<ScoringRule> rules = ruleRepository.findByEnabledTrueOrderByPriorityDesc();
        validateRules(rules);
        return rules;
    }

    private void validateRules(List<ScoringRule> rules) {
        if (rules.isEmpty()) {
            throw new IllegalStateException("No active scoring rules found in database");
        }
        
        rules.forEach(rule -> {
            if (rule.getRiskPoints() <= 0) {
                throw new IllegalStateException("Invalid risk points for rule: " + rule.getName());
            }
        });
    }
}