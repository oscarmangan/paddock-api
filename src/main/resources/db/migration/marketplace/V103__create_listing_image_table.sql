CREATE TABLE paddock.listing_image (
    id                BIGSERIAL       PRIMARY KEY,
    listing_id        BIGINT          NOT NULL REFERENCES paddock.listing(id),
    url               VARCHAR(500)    NOT NULL,
    is_thumbnail      BOOLEAN         NOT NULL DEFAULT FALSE,
    display_order     INTEGER         NOT NULL,
    created_at        TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_listing_image_listing_id ON paddock.listing_image(listing_id);