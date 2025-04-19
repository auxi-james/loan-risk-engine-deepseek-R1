package com.loanrisk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(RuleLoaderService.class)
@ActiveProfiles("test")
class RuleLoaderServiceTest {

    @Autowired
    private ScoringRuleRepository ruleRepository;

    @Autowired
    private RuleLoaderService ruleLoaderService;

    @BeforeEach
    void setUp() {
        ruleRepository.deleteAll();
        
        List<ScoringRule> rules = List.of(
            createRule("Credit Low", 1, true, 30),
            createRule("Debt High", 2, false, 20),
            createRule("Income Ratio", 3, true, 25)
        );
        
        ruleRepository.saveAll(rules);
    }

    private ScoringRule createRule(String name, int priority, boolean enabled, int points) {
        ScoringRule rule = new ScoringRule();
        rule.setName(name);
        rule.setField("creditScore");
        rule.setOperator("<");
        rule.setValue("600");
        rule.setPriority(priority);
        rule.setEnabled(enabled);
        rule.setRiskPoints(points);
        return rule;
    }

    @Test
    void shouldLoadOnlyActiveRulesOrderedByPriority() {
        List<ScoringRule> activeRules = ruleLoaderService.loadActiveRules();
        
        assertEquals(2, activeRules.size());
        assertEquals("Income Ratio", activeRules.get(0).getName());
        assertEquals("Credit Low", activeRules.get(1).getName());
    }

    @Test
    void shouldThrowWhenNoActiveRules() {
        ruleRepository.deleteAll();
        assertThrows(IllegalStateException.class, () -> ruleLoaderService.loadActiveRules());
    }

    @Test
    void shouldValidateRiskPoints() {
        ScoringRule invalidRule = createRule("Invalid Points", 4, true, 0);
        
        // Should fail validation on save
        assertThrows(jakarta.validation.ConstraintViolationException.class,
            () -> ruleRepository.save(invalidRule));
    }
}