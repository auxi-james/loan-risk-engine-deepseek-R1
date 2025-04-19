package com.loanrisk;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Positive(message = "Loan amount must be positive")
    private double loanAmount;
    
    @NotBlank(message = "Loan purpose is required")
    private String loanPurpose;
    
    @Min(value = 6, message = "Minimum loan term is 6 months")
    private int requestedTermMonths;
    private int riskScore;
    private String riskLevel;
    private String decision;
    
    @ElementCollection
    @CollectionTable(name = "risk_explanations", joinColumns = @JoinColumn(name = "loan_id"))
    @Column(name = "explanation")
    private List<String> explanations = new ArrayList<>();
    
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}