package com.tracktime.api.mapper;

import com.tracktime.api.dto.TrackDto;
import com.tracktime.api.dto.TrackLayoutDto;
import com.tracktime.api.model.Track;
import com.tracktime.api.model.TrackLayout;

import java.util.List;

public class TrackMapper {

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
                track.getTrackMapUrl(),
                track.getCreatedAt(),
                layouts
        );
    }
}