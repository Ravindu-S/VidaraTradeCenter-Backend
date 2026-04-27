-- Product-level subscription eligibility and discount (separate from core products table)
-- IF NOT EXISTS: safe when tables already exist (e.g. prior partial run or Hibernate) so Flyway can record success.
CREATE TABLE IF NOT EXISTS product_subscription_offers (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL UNIQUE REFERENCES products (id) ON DELETE CASCADE,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    discount_percent NUMERIC(5, 2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_product_subscription_offers_product ON product_subscription_offers (product_id);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    shipping_address_id BIGINT NOT NULL REFERENCES addresses (id) ON DELETE RESTRICT,
    frequency VARCHAR(30) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    status VARCHAR(20) NOT NULL,
    unit_price_snapshot NUMERIC(10, 2) NOT NULL,
    discount_percent_applied NUMERIC(5, 2) NOT NULL,
    next_billing_date DATE NOT NULL,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_subscriptions_user ON subscriptions (user_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_product ON subscriptions (product_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_status_next_billing ON subscriptions (status, next_billing_date);
