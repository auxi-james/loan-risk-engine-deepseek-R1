CREATE TABLE scoring_rule (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    field VARCHAR(255) NOT NULL,
    operator VARCHAR(10) NOT NULL,
    comparison_value TEXT,
    risk_points INT NOT NULL CHECK (risk_points > 0),
    priority INT NOT NULL CHECK (priority > 0),
    enabled BOOLEAN NOT NULL DEFAULT true
);