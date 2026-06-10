package com.paddock.api.dto;

import java.time.OffsetDateTime;

public record OrganiserDto(
        String id,
        String name,
        String websiteUrl,
        String logoUrl,
        OffsetDateTime createdAt
) {}