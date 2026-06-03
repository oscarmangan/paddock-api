package com.tracktime.api.web.controller;

import com.tracktime.api.dto.EventDto;
import com.tracktime.api.mapper.EventMapper;
import com.tracktime.api.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents().stream()
                .map(EventMapper::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(EventMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/track/{trackId}")
    public ResponseEntity<List<EventDto>> getEventsByTrack(@PathVariable String trackId) {
        return ResponseEntity.ok(eventService.getEventsByTrackId(trackId).stream()
                .map(EventMapper::toDto)
                .toList());
    }

    @GetMapping("/organiser/{organiserId}")
    public ResponseEntity<List<EventDto>> getEventsByOrganiser(@PathVariable String organiserId) {
        return ResponseEntity.ok(eventService.getEventsByOrganiserId(organiserId).stream()
                .map(EventMapper::toDto)
                .toList());
    }

    @GetMapping("/range")
    public ResponseEntity<List<EventDto>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(eventService.getEventsByDateRange(from, to).stream()
                .map(EventMapper::toDto)
                .toList());
    }

    @GetMapping("/track/{trackId}/range")
    public ResponseEntity<List<EventDto>> getEventsByTrackAndDateRange(
            @PathVariable String trackId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(eventService.getEventsByTrackAndDateRange(trackId, from, to).stream()
                .map(EventMapper::toDto)
                .toList());
    }
}