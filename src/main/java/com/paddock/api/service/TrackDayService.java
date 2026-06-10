package com.paddock.api.service;

import com.paddock.api.dto.TrackDayDto;
import com.paddock.api.dto.shared.PagedResponse;
import com.paddock.api.mapper.TrackDayMapper;
import com.paddock.api.model.TrackDay;
import com.paddock.api.repository.TrackDayRepository;
import com.paddock.api.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackDayService {

    private final TrackDayRepository trackDayRepository;

    public TrackDayDto getTrackDayById(Long id) {
        return trackDayRepository.findById(id)
                .map(TrackDayMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("TrackDay", id.toString()));
    }

    public PagedResponse<TrackDayDto> getAllTrackDays(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDatetime").ascending());
        Page<TrackDay> result = trackDayRepository.findAll(pageable);
        return toPagedResponse(result);
    }

    public PagedResponse<TrackDayDto> getTrackDaysByTrackId(String trackId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDatetime").ascending());
        Page<TrackDay> result = trackDayRepository.findByTrackId(trackId, pageable);
        return toPagedResponse(result);
    }

    public List<TrackDayDto> getTrackDaysByOrganiserId(String organiserId) {
        return trackDayRepository.findByOrganiserId(organiserId).stream()
                .map(TrackDayMapper::toDto)
                .toList();
    }

    public List<TrackDayDto> getTrackDaysByTrackAndDateRange(String trackId, OffsetDateTime from, OffsetDateTime to) {
        return trackDayRepository.findByTrackIdAndStartDatetimeBetween(trackId, from, to).stream()
                .map(TrackDayMapper::toDto)
                .toList();
    }

    private PagedResponse<TrackDayDto> toPagedResponse(Page<TrackDay> page) {
        List<TrackDayDto> content = page.getContent().stream()
                .map(TrackDayMapper::toDto)
                .toList();
        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
