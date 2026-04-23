-- Allow REFUNDED on orders.order_status and orders.payment_status.
-- Drops legacy CHECK constraints on those columns, then adds stable named constraints.

-- Drop any existing CHECK constraints that reference order_status
DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT c.conname
        FROM pg_constraint c
        JOIN pg_class t ON t.oid = c.conrelid
        WHERE t.relname = 'orders'
          AND c.contype = 'c'
          AND pg_get_constraintdef(c.oid) ILIKE '%order_status%'
    LOOP
        EXECUTE format('ALTER TABLE orders DROP CONSTRAINT %I', r.conname);
    END LOOP;
END $$;

-- Drop any existing CHECK constraints that reference payment_status
DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT c.conname
        FROM pg_constraint c
        JOIN pg_class t ON t.oid = c.conrelid
        WHERE t.relname = 'orders'
          AND c.contype = 'c'
          AND pg_get_constraintdef(c.oid) ILIKE '%payment_status%'
    LOOP
        EXECUTE format('ALTER TABLE orders DROP CONSTRAINT %I', r.conname);
    END LOOP;
END $$;

-- Recreate constraints (with stable names) including REFUNDED
ALTER TABLE orders
ADD CONSTRAINT orders_order_status_check
CHECK (order_status IN ('PENDING','PAID','PROCESSING','SHIPPED','DELIVERED','CANCELLED','REFUNDED'));

ALTER TABLE orders
ADD CONSTRAINT orders_payment_status_check
CHECK (payment_status IN ('PENDING','COMPLETED','FAILED','REFUNDED'));
