CREATE TABLE paddock.listing_report (
    id                BIGSERIAL       PRIMARY KEY,
    listing_id        BIGINT          NOT NULL REFERENCES paddock.listing(id),
    reporter_id       UUID            NOT NULL REFERENCES paddock.user(id),
    reason            VARCHAR(20)     NOT NULL CHECK (reason IN ('SPAM', 'INCORRECT_CATEGORY', 'PROHIBITED_ITEM', 'SUSPECTED_FRAUD', 'OTHER')),
    notes             TEXT,
    resolved_at       TIMESTAMPTZ,
    resolved_by       UUID            REFERENCES paddock.user(id),
    created_at        TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_listing_report_listing_id ON paddock.listing_report(listing_id);
CREATE INDEX idx_listing_report_resolved_at ON paddock.listing_report(resolved_at);