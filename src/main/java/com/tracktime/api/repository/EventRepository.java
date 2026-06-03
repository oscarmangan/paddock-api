package com.tracktime.api.repository;

import com.tracktime.api.model.Event;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@NullMarked
public interface EventRepository extends JpaRepository<Event, Long> {

    @Override
    Page<Event> findAll(Pageable pageable);

    Page<Event> findByTrackId(String trackId, Pageable pageable);

    List<Event> findByOrganiserId(String organiserId);

    List<Event> findByTrackIdAndStartDatetimeBetween(String trackId, OffsetDateTime from, OffsetDateTime to);
}