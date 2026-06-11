package com.paddock.api.web.controller;

import com.paddock.api.dto.TrackDto;
import com.paddock.api.dto.TrackLayoutDto;
import com.paddock.api.service.TrackLayoutService;
import com.paddock.api.service.TrackService;
import com.paddock.api.web.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;
    private final TrackLayoutService trackLayoutService;

    @Operation(summary = "Get all tracks")
    @ApiResponse(responseCode = "200", description = "List of tracks")
    @GetMapping
    public ResponseEntity<List<TrackDto>> getAllTracks() {
        return ResponseEntity.ok(trackService.getAllTracks());
    }

    @Operation(summary = "Get track by ID")
    @ApiResponse(responseCode = "200", description = "Track found")
    @ApiResponse(
            responseCode = "400",
            description = "Invalid track ID",
            content = @Content(schema = @Schema(implementation = ApiError.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Track not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<TrackDto> getTrackById(@PathVariable String id) {
        return ResponseEntity.ok(trackService.getTrackById(id));
    }

    @Operation(summary = "Get tracks by region")
    @ApiResponse(responseCode = "200", description = "List of all tracks filtered by region")
    @GetMapping("/region/{region}")
    public ResponseEntity<List<TrackDto>> getTracksByRegion(@PathVariable String region) {
        return ResponseEntity.ok(trackService.getTracksByRegion(region));
    }

    @Operation(summary = "Get track layouts")
    @ApiResponse(responseCode = "200", description = "List of layouts of a track")
    @GetMapping("/{id}/layouts")
    public ResponseEntity<List<TrackLayoutDto>> getLayoutsByTrack(@PathVariable String id) {
        return ResponseEntity.ok(trackLayoutService.getLayoutsByTrackId(id));
    }
}