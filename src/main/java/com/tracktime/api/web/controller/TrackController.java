package com.tracktime.api.web.controller;

import com.tracktime.api.dto.TrackDto;
import com.tracktime.api.dto.TrackLayoutDto;
import com.tracktime.api.mapper.TrackMapper;
import com.tracktime.api.service.TrackLayoutService;
import com.tracktime.api.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;
    private final TrackLayoutService trackLayoutService;

    @GetMapping
    public ResponseEntity<List<TrackDto>> getAllTracks() {
        return ResponseEntity.ok(trackService.getAllTracks().stream()
                .map(TrackMapper::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackDto> getTrackById(@PathVariable String id) {
        return trackService.getTrackById(id)
                .map(TrackMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<TrackDto>> getTracksByRegion(@PathVariable String region) {
        return ResponseEntity.ok(trackService.getTracksByRegion(region).stream()
                .map(TrackMapper::toDto)
                .toList());
    }

    @GetMapping("/{id}/layouts")
    public ResponseEntity<List<TrackLayoutDto>> getLayoutsByTrack(@PathVariable String id) {
        return ResponseEntity.ok(trackLayoutService.getLayoutsByTrackId(id).stream()
                .map(TrackMapper::toLayoutDto)
                .toList());
    }
}