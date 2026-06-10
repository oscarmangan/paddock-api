package com.paddock.api.repository;

import com.paddock.api.model.TrackLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackLayoutRepository extends JpaRepository<TrackLayout, String> {

    List<TrackLayout> findByTrackId(String trackId);

}
