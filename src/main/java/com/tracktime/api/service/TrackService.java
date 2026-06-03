package com.tracktime.api.service;

import com.tracktime.api.model.Track;
import com.tracktime.api.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    public Optional<Track> getTrackById(String id) {
        return trackRepository.findById(id);
    }

    public List<Track> getTracksByRegion(String region) {
        return trackRepository.findByRegion(region);
    }
}
