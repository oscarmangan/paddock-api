package com.paddock.api.repository;

import com.paddock.api.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {

    @Query("SELECT t FROM Track t LEFT JOIN FETCH t.layouts")
    List<Track> findAllWithLayouts();

    List<Track> findByRegion(String region);

}
