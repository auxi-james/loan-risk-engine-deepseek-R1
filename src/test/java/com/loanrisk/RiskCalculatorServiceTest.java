package com.loanrisk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiskCalculatorServiceTest {
    @Mock
    private RuleLoaderService ruleLoaderService;
    
    @InjectMocks
    private RiskCalculatorService riskCalculatorService;
    
    private LoanApplication application;
    private Customer customer;

    private ScoringRule createRule(String name, String field, String operator, String value, int points, int priority) {
        ScoringRule rule = new ScoringRule();
        rule.setName(name);
        rule.setField(field);
        rule.setOperator(operator);
        rule.setValue(value);
        rule.setRiskPoints(points);
        rule.setPriority(priority);
        rule.setEnabled(true);
        return rule;
    }

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setCreditScore(650);
        customer.setAnnualIncome(75000);
        customer.setExistingDebt(30000);
        
        application = new LoanApplication();
        application.setCustomer(customer);
        application.setLoanAmount(15000);
        application.setLoanPurpose("car");
    }

    @Test
    void shouldCalculateLowRiskForGoodCredit() {
        when(ruleLoaderService.loadActiveRules()).thenReturn(List.of(
            createRule("Credit Score < 600", "creditScore", "<", "600", 30, 1)
        ));
        
        var result = riskCalculatorService.calculateRisk(application);
        
        assertEquals(0, result.totalPoints());
        assertEquals("Low", result.riskLevel());
        assertEquals("approve", result.decision());
        assertTrue(result.explanations().isEmpty());
    }

    @Test
    void shouldAccumulatePointsFromMultipleRules() {
        when(ruleLoaderService.loadActiveRules()).thenReturn(List.of(
            createRule("High Debt", "debtToIncomeRatio", ">", "0.4", 20, 1),
            createRule("Car Loan", "loanPurpose", "==", "car", 10, 2)
        ));
        
        customer.setExistingDebt(40000); // debtToIncomeRatio = 40000/75000 = 0.53
        
        var result = riskCalculatorService.calculateRisk(application);
        
        assertEquals(30, result.totalPoints());
        assertEquals("Low", result.riskLevel()); // 30 is upper bound of Low
        assertEquals("approve", result.decision());
        assertEquals(2, result.explanations().size());
    }

    @Test
    void shouldHandleDifferentDataTypes() {
        when(ruleLoaderService.loadActiveRules()).thenReturn(List.of(
            createRule("Numeric Comparison", "creditScore", "<", "700", 15, 1),
            createRule("String Comparison", "loanPurpose", "==", "car", 10, 2)
        ));
        
        var result = riskCalculatorService.calculateRisk(application);
        
        assertEquals(25, result.totalPoints());
        assertEquals("Medium", result.riskLevel());
        assertEquals(2, result.explanations().size());
    }

    @Test
    void shouldRejectHighRiskApplications() {
        when(ruleLoaderService.loadActiveRules()).thenReturn(List.of(
            createRule("Very High Debt", "debtToIncomeRatio", ">", "0.6", 40, 1),
            createRule("Vacation Loan", "loanPurpose", "==", "vacation", 20, 2)
        ));
        
        application.setLoanPurpose("vacation");
        customer.setExistingDebt(50000); // debtToIncomeRatio = 50000/75000 = 0.66
        
        var result = riskCalculatorService.calculateRisk(application);
        
        assertEquals(60, result.totalPoints());
        assertEquals("High", result.riskLevel());
        assertEquals("reject", result.decision());
    }
}