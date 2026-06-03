package com.tracktime.api.service;

import com.tracktime.api.model.TrackLayout;
import com.tracktime.api.repository.TrackLayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackLayoutService {

    private final TrackLayoutRepository trackLayoutRepository;

    public List<TrackLayout> getAllLayouts() {
        return trackLayoutRepository.findAll();
    }

    public Optional<TrackLayout> getLayoutById(String id) {
        return trackLayoutRepository.findById(id);
    }

    public List<TrackLayout> getLayoutsByTrackId(String trackId) {
        return trackLayoutRepository.findByTrackId(trackId);
    }

}
