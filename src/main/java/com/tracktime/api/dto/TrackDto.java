package com.tracktime.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record TrackDto(
        String id,
        String name,
        String region,
        String trackMapUrl,
        OffsetDateTime createdAt,
        List<TrackLayoutDto> layouts
) {}