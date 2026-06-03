package com.tracktime.api.service;

import com.tracktime.api.dto.TrackLayoutDto;
import com.tracktime.api.mapper.TrackMapper;
import com.tracktime.api.repository.TrackLayoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackLayoutService {

    private final TrackLayoutRepository trackLayoutRepository;

    public List<TrackLayoutDto> getLayoutsByTrackId(String trackId) {
        return trackLayoutRepository.findByTrackId(trackId).stream()
                .map(TrackMapper::toLayoutDto)
                .toList();
    }

}
