CREATE TABLE paddock.vehicle_detail (
    id                BIGSERIAL       PRIMARY KEY,
    listing_id        BIGINT          NOT NULL UNIQUE REFERENCES paddock.listing(id),
    make              VARCHAR(100)    NOT NULL,
    model             VARCHAR(100)    NOT NULL,
    year              SMALLINT        NOT NULL,
    spec_class        VARCHAR(100)
);