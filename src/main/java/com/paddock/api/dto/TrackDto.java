package com.paddock.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record TrackDto(
        String id,
        String name,
        String region,
        String locale,
        Double latitude,
        Double longitude,
        String trackMapUrl,
        OffsetDateTime createdAt,
        List<TrackLayoutDto> layouts
) {}