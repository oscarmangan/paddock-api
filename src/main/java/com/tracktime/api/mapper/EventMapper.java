package com.tracktime.api.mapper;

import com.tracktime.api.dto.EventDto;
import com.tracktime.api.model.Event;

public class EventMapper {

    public static EventDto toDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getTrack().getId(),
                event.getTrack().getName(),
                event.getTrackLayout() != null ? event.getTrackLayout().getId() : null,
                event.getTrackLayout() != null ? event.getTrackLayout().getName() : null,
                event.getOrganiser().getId(),
                event.getOrganiser().getName(),
                event.getEventName(),
                event.getStartDatetime(),
                event.getEndDatetime(),
                event.getBookingUrl(),
                event.getSessionType(),
                event.getNoiseLimitStaticDecibels(),
                event.getNoiseLimitDriveByDecibels(),
                event.isSoldOut(),
                event.getSource(),
                event.getLastScrapedAt(),
                event.isVerified(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}