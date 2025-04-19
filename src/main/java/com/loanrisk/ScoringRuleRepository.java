package com.loanrisk;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScoringRuleRepository extends JpaRepository<ScoringRule, Long> {
    List<ScoringRule> findByEnabledTrueOrderByPriorityDesc();
}