package com.paddock.api.mapper;

import com.paddock.api.dto.TrackDayDto;
import com.paddock.api.model.TrackDay;

public class TrackDayMapper {

    private TrackDayMapper() {}

    public static TrackDayDto toDto(TrackDay trackDay) {
        return new TrackDayDto(
                trackDay.getId(),
                trackDay.getTrack().getId(),
                trackDay.getTrack().getName(),
                trackDay.getTrackLayout() != null ? trackDay.getTrackLayout().getId() : null,
                trackDay.getTrackLayout() != null ? trackDay.getTrackLayout().getName() : null,
                trackDay.getOrganiser().getId(),
                trackDay.getOrganiser().getName(),
                trackDay.getTrackDayName(),
                trackDay.getStartDatetime(),
                trackDay.getEndDatetime(),
                trackDay.getBookingUrl(),
                trackDay.getSessionType(),
                trackDay.getNoiseLimitStaticDecibels(),
                trackDay.getNoiseLimitDriveByDecibels(),
                trackDay.isSoldOut(),
                trackDay.getSource(),
                trackDay.getLastScrapedAt(),
                trackDay.isVerified(),
                trackDay.getCreatedAt(),
                trackDay.getUpdatedAt()
        );
    }
}
