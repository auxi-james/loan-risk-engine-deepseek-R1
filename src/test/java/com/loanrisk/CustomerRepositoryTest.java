package com.loanrisk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        entityManager.clear();
        testCustomer = new Customer();
        testCustomer.setName("John Doe");
        testCustomer.setAge(30);
        testCustomer.setAnnualIncome(75000);
        testCustomer.setCreditScore(720);
        testCustomer.setEmploymentStatus(Customer.EmploymentStatus.EMPLOYED);
        testCustomer.setExistingDebt(15000);
    }

    @Test
    void testEmptyDatabase() {
        assertEquals(0, customerRepository.count());
    }

    @Test
    void testSaveCustomer() {
        Customer saved = customerRepository.save(testCustomer);
        assertNotNull(saved.getId());
        assertEquals("John Doe", saved.getName());
        assertEquals(1, customerRepository.count());
    }

    @Test
    void testFindByName() {
        customerRepository.save(testCustomer);
        Customer found = customerRepository.findByName("John Doe").get(0);
        assertEquals(720, found.getCreditScore());
        assertEquals(75000, found.getAnnualIncome(), 0.001);
    }
}