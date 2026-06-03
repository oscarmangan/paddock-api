package com.tracktime.api.repository;

import com.tracktime.api.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {

    List<Track> findByRegion(String region);

}
