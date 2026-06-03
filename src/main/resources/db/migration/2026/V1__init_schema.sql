CREATE TABLE tracktime.organiser (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    website_url VARCHAR(500),
    logo_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE tracktime.track (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    region VARCHAR(100),
    track_map_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE tracktime.track_layout (
    id VARCHAR(20) PRIMARY KEY,
    track_id VARCHAR(20) NOT NULL REFERENCES tracktime.track(id),
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE tracktime.event (
    id BIGSERIAL PRIMARY KEY,
    track_id VARCHAR(20) NOT NULL REFERENCES tracktime.track(id),
    track_layout_id VARCHAR(20) REFERENCES tracktime.track_layout(id),
    organiser_id VARCHAR(20) NOT NULL REFERENCES tracktime.organiser(id),
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

CREATE UNIQUE INDEX idx_event_booking_url ON tracktime.event(booking_url);
CREATE INDEX idx_event_track_id ON tracktime.event(track_id);
CREATE INDEX idx_event_organiser_id ON tracktime.event(organiser_id);
CREATE INDEX idx_event_start_datetime ON tracktime.event(start_datetime);
