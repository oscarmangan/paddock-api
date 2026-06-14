package com.paddock.api.mapper;

import com.paddock.api.dto.TrackDto;
import com.paddock.api.dto.TrackLayoutDto;
import com.paddock.api.model.Track;
import com.paddock.api.model.TrackLayout;

import java.util.List;

public class TrackMapper {

    private TrackMapper() {}

    public static TrackLayoutDto toLayoutDto(TrackLayout layout) {
        return new TrackLayoutDto(
                layout.getId(),
                layout.getName(),
                layout.getCreatedAt()
        );
    }

    public static TrackDto toDto(Track track) {
        List<TrackLayoutDto> layouts = track.getLayouts() != null
                ? track.getLayouts().stream().map(TrackMapper::toLayoutDto).toList()
                : List.of();

        return new TrackDto(
                track.getId(),
                track.getName(),
                track.getRegion(),
                track.getLocale(),
                track.getLatitude(),
                track.getLongitude(),
                track.getTrackMapUrl(),
                track.getCreatedAt(),
                layouts
        );
    }
}