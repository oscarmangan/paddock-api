CREATE TABLE paddock.listing_watchlist (
    id                BIGSERIAL       PRIMARY KEY,
    user_id           UUID            NOT NULL REFERENCES paddock.user(id),
    listing_id        BIGINT          NOT NULL REFERENCES paddock.listing(id),
    created_at        TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    UNIQUE (user_id, listing_id)
);

CREATE INDEX idx_listing_watchlist_user_id ON paddock.listing_watchlist(user_id);