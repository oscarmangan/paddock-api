package com.paddock.api.mapper;

import com.paddock.api.dto.OrganiserDto;
import com.paddock.api.model.Organiser;

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