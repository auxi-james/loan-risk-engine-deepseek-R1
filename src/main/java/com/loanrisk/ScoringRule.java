package com.loanrisk;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class ScoringRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Rule name is required")
    private String name;
    
    @NotBlank(message = "Field name is required")
    private String field;
    
    @NotBlank(message = "Operator is required")
    private String operator;
    
    @Column(name = "`value`")
    private String value;
    
    @Positive(message = "Risk points must be positive")
    @Positive(message = "Risk points must be positive")
    private int riskPoints;
    
    @Positive(message = "Priority must be positive")
    private int priority;
    
    private boolean enabled = true;

    public Object getParsedValue() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }
}