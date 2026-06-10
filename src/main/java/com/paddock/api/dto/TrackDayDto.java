package com.paddock.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TrackDayDto(
        Long id,
        String trackId,
        String trackName,
        String trackLayoutId,
        String trackLayoutName,
        String organiserId,
        String organiserName,
        String trackDayName,
        OffsetDateTime startDatetime,
        OffsetDateTime endDatetime,
        String bookingUrl,
        String sessionType,
        BigDecimal noiseLimitStaticDecibels,
        BigDecimal noiseLimitDriveByDecibels,
        boolean isSoldOut,
        String source,
        OffsetDateTime lastScrapedAt,
        boolean isVerified,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
