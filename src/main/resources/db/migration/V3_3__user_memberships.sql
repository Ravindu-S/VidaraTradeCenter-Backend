-- Store-level membership (discount on all products); stacks with bulk quantity tiers at checkout
-- IF NOT EXISTS: safe when Hibernate or a prior run created these objects before Flyway recorded the version.
CREATE TABLE IF NOT EXISTS user_memberships (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    plan VARCHAR(32) NOT NULL,
    billing_period VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_user_memberships_user ON user_memberships (user_id);
CREATE INDEX IF NOT EXISTS idx_user_memberships_user_status ON user_memberships (user_id, status);
