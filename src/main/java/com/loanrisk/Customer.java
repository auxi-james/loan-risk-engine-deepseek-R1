package com.loanrisk;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @Min(value = 18, message = "Age must be at least 18")
    private int age;
    
    @Positive(message = "Annual income must be positive")
    private double annualIncome;
    
    @Min(300) @Max(850)
    private int creditScore;
    
    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;
    private double existingDebt;
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<LoanApplication> loanApplications = new ArrayList<>();
    
    public enum EmploymentStatus {
        EMPLOYED, SELF_EMPLOYED, UNEMPLOYED, RETIRED
    }
}