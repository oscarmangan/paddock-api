package com.tracktime.api.web.controller;

import com.tracktime.api.dto.EventDto;
import com.tracktime.api.dto.shared.PagedResponse;
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


    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<EventDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(eventService.getAllEvents(page, size));
    }

    @GetMapping("/track/{trackId}")
    public ResponseEntity<PagedResponse<EventDto>> getEventsByTrack(
            @PathVariable String trackId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(eventService.getEventsByTrackId(trackId, page, size));
    }

    @GetMapping("/organiser/{organiserId}")
    public ResponseEntity<List<EventDto>> getEventsByOrganiser(@PathVariable String organiserId) {
        return ResponseEntity.ok(eventService.getEventsByOrganiserId(organiserId));
    }

    @GetMapping("/track/{trackId}/range")
    public ResponseEntity<List<EventDto>> getEventsByTrackAndDateRange(
            @PathVariable String trackId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(eventService.getEventsByTrackAndDateRange(trackId, from, to));
    }
}