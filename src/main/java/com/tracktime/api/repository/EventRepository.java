package com.tracktime.api.repository;

import com.tracktime.api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByBookingUrl(String bookingUrl);

    List<Event> findByTrackId(String trackId);

    List<Event> findByOrganiserId(String organiserId);

    List<Event> findByStartDatetimeBetween(OffsetDateTime from, OffsetDateTime to);

    List<Event> findByTrackIdAndStartDatetimeBetween(String trackId, OffsetDateTime from, OffsetDateTime to);
}