package com.loanrisk;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskCalculatorService {
    private final RuleLoaderService ruleLoaderService;
    
    public RiskCalculationResult calculateRisk(LoanApplication application) {
        List<ScoringRule> rules = ruleLoaderService.loadActiveRules();
        int totalPoints = 0;
        List<String> explanations = new ArrayList<>();
        
        for (ScoringRule rule : rules) {
            Object actualValue = getFieldValue(rule.getField(), application);
            Object ruleValue = rule.getParsedValue();
            
            if (evaluateCondition(rule.getOperator(), actualValue, ruleValue)) {
                totalPoints += rule.getRiskPoints();
                explanations.add(rule.getName() + " (" + rule.getRiskPoints() + " points)");
            }
        }
        
        return new RiskCalculationResult(
            totalPoints,
            determineRiskLevel(totalPoints),
            determineDecision(totalPoints),
            explanations
        );
    }

    private Object getFieldValue(String field, LoanApplication application) {
        return switch (field) {
            case "creditScore" -> application.getCustomer().getCreditScore();
            case "loanAmount" -> application.getLoanAmount();
            case "loanPurpose" -> application.getLoanPurpose();
            case "existingDebt" -> application.getCustomer().getExistingDebt();
            case "annualIncome" -> application.getCustomer().getAnnualIncome();
            case "debtToIncomeRatio" -> calculateDebtToIncomeRatio(application);
            default -> throw new IllegalArgumentException("Unknown field: " + field);
        };
    }

    private double calculateDebtToIncomeRatio(LoanApplication application) {
        Customer customer = application.getCustomer();
        if (customer.getAnnualIncome() == 0) return 0;
        return customer.getExistingDebt() / customer.getAnnualIncome();
    }

    private boolean evaluateCondition(String operator, Object actual, Object ruleValue) {
        if (actual instanceof Number && ruleValue instanceof Number) {
            double a = ((Number) actual).doubleValue();
            double r = ((Number) ruleValue).doubleValue();
            return switch (operator) {
                case "<" -> a < r;
                case ">" -> a > r;
                case "<=" -> a <= r;
                case ">=" -> a >= r;
                case "==" -> a == r;
                default -> false;
            };
        }
        return actual.toString().equals(ruleValue.toString());
    }

    private String determineRiskLevel(int points) {
        if (points <= 30) return "Low";
        if (points <= 60) return "Medium";
        return "High";
    }

    private String determineDecision(int points) {
        if (points <= 30) return "approve";
        if (points <= 60) return "manual_review";
        return "reject";
    }
    
    public record RiskCalculationResult(
        int totalPoints,
        String riskLevel,
        String decision,
        List<String> explanations
    ) {}
}