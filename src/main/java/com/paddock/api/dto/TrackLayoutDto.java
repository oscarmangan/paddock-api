package com.paddock.api.dto;

import java.time.OffsetDateTime;

public record TrackLayoutDto(
        String id,
        String name,
        OffsetDateTime createdAt
) {}