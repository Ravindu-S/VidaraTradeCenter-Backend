-- Pending / completed PayHere flows for store membership (order_id prefix MS)
CREATE TABLE IF NOT EXISTS membership_payment_intents (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    plan VARCHAR(32) NOT NULL,
    billing_period VARCHAR(20) NOT NULL,
    amount NUMERIC(14, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_membership_intents_user ON membership_payment_intents (user_id);
CREATE INDEX IF NOT EXISTS idx_membership_intents_status ON membership_payment_intents (status);
