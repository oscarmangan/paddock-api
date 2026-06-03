package com.tracktime.api.service;

import com.tracktime.api.dto.EventDto;
import com.tracktime.api.dto.shared.PagedResponse;
import com.tracktime.api.mapper.EventMapper;
import com.tracktime.api.model.Event;
import com.tracktime.api.repository.EventRepository;
import com.tracktime.api.web.exception.ResourceNotFoundException;
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
public class EventService {

    private final EventRepository eventRepository;

    public EventDto getEventById(Long id) {
        return eventRepository.findById(id)
                .map(EventMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Event", id.toString()));
    }

    public PagedResponse<EventDto> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDatetime").ascending());
        Page<Event> result = eventRepository.findAll(pageable);
        return toPagedResponse(result);
    }

    public PagedResponse<EventDto> getEventsByTrackId(String trackId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startDatetime").ascending());
        Page<Event> result = eventRepository.findByTrackId(trackId, pageable);
        return toPagedResponse(result);
    }

    public List<EventDto> getEventsByOrganiserId(String organiserId) {
        return eventRepository.findByOrganiserId(organiserId).stream()
                .map(EventMapper::toDto)
                .toList();
    }

    public List<EventDto> getEventsByTrackAndDateRange(String trackId, OffsetDateTime from, OffsetDateTime to) {
        return eventRepository.findByTrackIdAndStartDatetimeBetween(trackId, from, to).stream()
                .map(EventMapper::toDto)
                .toList();
    }

    private PagedResponse<EventDto> toPagedResponse(Page<Event> page) {
        List<EventDto> content = page.getContent().stream()
                .map(EventMapper::toDto)
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