package com.paddock.api.web.controller;

import com.paddock.api.dto.TrackDayDto;
import com.paddock.api.dto.shared.PagedResponse;
import com.paddock.api.service.TrackDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/track-days")
@RequiredArgsConstructor
public class TrackDayController {

    private final TrackDayService trackDayService;

    @GetMapping("/{id}")
    public ResponseEntity<TrackDayDto> getTrackDayById(@PathVariable Long id) {
        return ResponseEntity.ok(trackDayService.getTrackDayById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<TrackDayDto>> getAllTrackDays(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(trackDayService.getAllTrackDays(page, size));
    }

    @GetMapping("/track/{trackId}")
    public ResponseEntity<PagedResponse<TrackDayDto>> getTrackDaysByTrack(
            @PathVariable String trackId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(trackDayService.getTrackDaysByTrackId(trackId, page, size));
    }

    @GetMapping("/organiser/{organiserId}")
    public ResponseEntity<List<TrackDayDto>> getTrackDaysByOrganiser(@PathVariable String organiserId) {
        return ResponseEntity.ok(trackDayService.getTrackDaysByOrganiserId(organiserId));
    }

    @GetMapping("/track/{trackId}/range")
    public ResponseEntity<List<TrackDayDto>> getTrackDaysByTrackAndDateRange(
            @PathVariable String trackId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return ResponseEntity.ok(trackDayService.getTrackDaysByTrackAndDateRange(trackId, from, to));
    }
}
