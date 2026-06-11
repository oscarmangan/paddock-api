package com.paddock.api.dto.marketplace;

import java.time.OffsetDateTime;

public record ListingDto(
        Long id,
        String discipline,
        String categoryName,
        String categorySlug,
        String title,
        String slug,
        Integer price,
        String priceType,
        String currency,
        String town,
        String county,
        String listingTier,
        String thumbnailUrl,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt
) {}
