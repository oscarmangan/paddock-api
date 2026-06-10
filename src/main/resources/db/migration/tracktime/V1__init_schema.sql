CREATE TABLE IF NOT EXISTS paddock.organiser (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    website_url VARCHAR(500),
    logo_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS paddock.track (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    region VARCHAR(100),
    track_map_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS paddock.track_layout (
    id VARCHAR(20) PRIMARY KEY,
    track_id VARCHAR(20) NOT NULL REFERENCES paddock.track(id),
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS paddock.track_day (
    id BIGSERIAL PRIMARY KEY,
    track_id VARCHAR(20) NOT NULL REFERENCES paddock.track(id),
    track_layout_id VARCHAR(20) REFERENCES paddock.track_layout(id),
    organiser_id VARCHAR(20) NOT NULL REFERENCES paddock.organiser(id),
    event_name VARCHAR(500),
    start_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    end_datetime TIMESTAMP WITH TIME ZONE,
    booking_url VARCHAR(500) NOT NULL,
    session_type VARCHAR(50),
    noise_limit_static_db INTEGER,
    noise_limit_driveby_db INTEGER,
    is_sold_out BOOLEAN NOT NULL DEFAULT FALSE,
    source VARCHAR(20) NOT NULL DEFAULT 'scraped',
    last_scraped_at TIMESTAMP WITH TIME ZONE,
    is_verified BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_event_booking_url ON paddock.track_day(booking_url);
CREATE INDEX IF NOT EXISTS idx_track_day_track_id ON paddock.track_day(track_id);
CREATE INDEX IF NOT EXISTS idx_track_day_organiser_id ON paddock.track_day(organiser_id);
CREATE INDEX IF NOT EXISTS idx_track_day_start_datetime ON paddock.track_day(start_datetime);
