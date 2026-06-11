ALTER TABLE paddock.listing ADD COLUMN discipline VARCHAR(20);

-- Backfill discipline from category for all existing rows
UPDATE paddock.listing l
SET discipline = (
    SELECT c.discipline
    FROM paddock.category c
    WHERE c.id = l.category_id
);

ALTER TABLE paddock.listing ALTER COLUMN discipline SET NOT NULL;
ALTER TABLE paddock.listing ADD CONSTRAINT chk_listing_discipline
    CHECK (discipline IN ('race', 'rally', 'drift', 'kart', 'transport', 'racewear'));

CREATE INDEX idx_listing_discipline ON paddock.listing (discipline);