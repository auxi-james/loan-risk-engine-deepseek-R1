package com.loanrisk.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than 100")
    private int age;

    @PositiveOrZero(message = "Annual income cannot be negative")
    private double annualIncome;

    @Min(300) @Max(850)
    private int creditScore;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus employmentStatus;

    @PositiveOrZero
    private double existingDebt;

    public enum EmploymentStatus {
        EMPLOYED, SELF_EMPLOYED, UNEMPLOYED, RETIRED
    }
}