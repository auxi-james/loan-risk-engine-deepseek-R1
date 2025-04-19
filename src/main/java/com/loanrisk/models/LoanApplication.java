package com.loanrisk.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Positive(message = "Loan amount must be positive")
    private double loanAmount;

    @NotBlank(message = "Loan purpose is required")
    private String loanPurpose;

    @Min(value = 6, message = "Minimum loan term is 6 months")
    @Max(value = 60, message = "Maximum loan term is 60 months")
    private int requestedTermMonths;

    private Integer riskScore;
    private String riskLevel;
    private String decision;
    
    @ElementCollection
    private List<String> explanation;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}