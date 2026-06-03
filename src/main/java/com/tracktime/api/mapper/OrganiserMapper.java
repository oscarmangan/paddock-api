package com.tracktime.api.mapper;

import com.tracktime.api.dto.OrganiserDto;
import com.tracktime.api.model.Organiser;

public class OrganiserMapper {

    public static OrganiserDto toDto(Organiser organiser) {
        return new OrganiserDto(
                organiser.getId(),
                organiser.getName(),
                organiser.getWebsiteUrl(),
                organiser.getLogoUrl(),
                organiser.getCreatedAt()
        );
    }
}