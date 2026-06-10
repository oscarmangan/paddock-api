ALTER TABLE paddock.listing ADD COLUMN search_vector TSVECTOR;

CREATE INDEX idx_listing_search_vector ON paddock.listing USING GIN(search_vector);

CREATE OR REPLACE FUNCTION paddock.update_listing_search_vector()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.search_vector := to_tsvector('english', COALESCE(NEW.title, '') || ' ' || COALESCE(NEW.description, ''));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_listing_search_vector
    BEFORE INSERT OR UPDATE ON paddock.listing
    FOR EACH ROW EXECUTE FUNCTION paddock.update_listing_search_vector();