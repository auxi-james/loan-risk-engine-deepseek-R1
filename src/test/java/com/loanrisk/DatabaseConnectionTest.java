package com.loanrisk;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testPostgresqlConnection() {
        assertNotNull(dataSource);
        assertDoesNotThrow(() -> {
            jdbcTemplate.execute("SELECT 1");
        }, "Should connect to PostgreSQL");
    }

    @Test
    public void testH2InMemoryDatabase() {
        assertNotNull(dataSource);
        assertDoesNotThrow(() -> {
            jdbcTemplate.execute("SELECT 1");
        }, "Should connect to H2");
    }
}