package com.paddock.api.service;

import com.paddock.api.dto.TrackDto;
import com.paddock.api.mapper.TrackMapper;
import com.paddock.api.repository.TrackRepository;
import com.paddock.api.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;

    public List<TrackDto> getAllTracks() {
        return trackRepository.findAllWithLayouts().stream()
                .map(TrackMapper::toDto)
                .toList();
    }

    public TrackDto getTrackById(String id) {
        return trackRepository.findById(id)
                .map(TrackMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Track", id));
    }

    public List<TrackDto> getTracksByRegion(String region) {
        return trackRepository.findByRegion(region).stream()
                .map(TrackMapper::toDto)
                .toList();
    }
}
