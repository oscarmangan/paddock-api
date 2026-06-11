package com.paddock.api.dto.marketplace;

import java.time.OffsetDateTime;
import java.util.List;

public record ListingDetailDto(
        Long id,
        String discipline,
        String categoryName,
        String categorySlug,
        String title,
        String slug,
        String description,
        Integer price,
        String priceType,
        String currency,
        boolean inclVat,
        String town,
        String county,
        String listingTier,
        List<String> imageUrls,
        String sellerDisplayName,
        String phoneNumber,
        OffsetDateTime memberSince,
        VehicleDetailDto vehicleDetail,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt
) {}
