package com.paddock.api.dto.marketplace;

public record ListingFilter(
        Integer minPrice,
        Integer maxPrice,
        String county
) {}
