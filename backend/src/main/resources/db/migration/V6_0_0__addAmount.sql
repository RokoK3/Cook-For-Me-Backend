ALTER TABLE IF EXISTS order_rating
    ADD COLUMN IF NOT EXISTS amount INT default 1;