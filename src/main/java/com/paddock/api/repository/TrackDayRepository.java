package com.paddock.api.repository;

import com.paddock.api.model.TrackDay;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@NullMarked
public interface TrackDayRepository extends JpaRepository<TrackDay, Long> {

    @Override
    Page<TrackDay> findAll(Pageable pageable);

    Page<TrackDay> findByTrackId(String trackId, Pageable pageable);

    List<TrackDay> findByOrganiserId(String organiserId);

    List<TrackDay> findByTrackIdAndStartDatetimeBetween(String trackId, OffsetDateTime from, OffsetDateTime to);
}
