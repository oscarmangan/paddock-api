CREATE TABLE paddock.category (
    id                BIGSERIAL       PRIMARY KEY,
    discipline        VARCHAR(20)     NOT NULL CHECK (discipline IN ('race', 'rally', 'drift', 'kart', 'transport', 'racewear')),
    parent_id         BIGINT          REFERENCES paddock.category(id),
    name              VARCHAR(100)    NOT NULL,
    label             VARCHAR(100)    NOT NULL,
    slug              VARCHAR(100)    NOT NULL,
    display_order     INTEGER         NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX idx_category_discipline_slug ON paddock.category(discipline, slug);