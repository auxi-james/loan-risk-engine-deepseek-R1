package com.loanrisk.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScoringRuleTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validScoringRuleShouldPass() {
        ScoringRule rule = new ScoringRule();
        rule.setName("Credit Score Check");
        rule.setField("creditScore");
        rule.setOperator(">");
        rule.setComparisonValue("600");
        rule.setRiskPoints(20);
        rule.setPriority(1);
        
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(rule);
        assertTrue(violations.isEmpty());
    }

    @Test
    void invalidScoringRuleShouldFail() {
        ScoringRule rule = new ScoringRule();
        rule.setRiskPoints(-5); // Invalid value
        
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(rule);
        assertEquals(3, violations.size()); // Checks for missing name, field, operator
    }
}