package com.tracktime.api.service;

import com.tracktime.api.dto.TrackDto;
import com.tracktime.api.mapper.TrackMapper;
import com.tracktime.api.repository.TrackRepository;
import com.tracktime.api.web.exception.ResourceNotFoundException;
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
