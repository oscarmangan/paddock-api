CREATE TABLE paddock.listing (
    id             BIGSERIAL PRIMARY KEY,
    seller_id      UUID         NOT NULL REFERENCES paddock.user (id),
    category_id    BIGINT       NOT NULL REFERENCES paddock.category (id),
    title          VARCHAR(60) NOT NULL,
    slug           VARCHAR(80) NOT NULL,
    description    TEXT         NOT NULL,
    price          INTEGER,
    price_type     VARCHAR(10)  NOT NULL CHECK (price_type IN ('FIXED', 'POA', 'FREE', 'WANTED')),
    currency       VARCHAR(3)   NOT NULL CHECK (currency IN ('GBP', 'EUR')),
    incl_vat       BOOLEAN      NOT NULL DEFAULT FALSE,
    town           VARCHAR(100) NOT NULL,
    county         VARCHAR(100) NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'ACTIVE', 'EXPIRED', 'SOLD', 'REMOVED')),
    listing_tier   VARCHAR(10)  NOT NULL DEFAULT 'STANDARD' CHECK (listing_tier IN ('STANDARD', 'FEATURED')),
    bump_count     INTEGER      NOT NULL DEFAULT 0,
    last_bumped_at TIMESTAMPTZ,
    view_count     INTEGER      NOT NULL DEFAULT 0,
    report_count   INTEGER      NOT NULL DEFAULT 0,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    expires_at     TIMESTAMPTZ  NOT NULL,
    extended_at    TIMESTAMPTZ,
    sold_at        TIMESTAMPTZ,
    updated_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_listing_seller_id ON paddock.listing (seller_id);
CREATE INDEX idx_listing_category_id ON paddock.listing (category_id);
CREATE INDEX idx_listing_status ON paddock.listing (status);
CREATE INDEX idx_listing_created_at ON paddock.listing (created_at);