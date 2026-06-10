package com.paddock.api.service;

import com.paddock.api.dto.TrackLayoutDto;
import com.paddock.api.mapper.TrackMapper;
import com.paddock.api.repository.TrackLayoutRepository;
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
