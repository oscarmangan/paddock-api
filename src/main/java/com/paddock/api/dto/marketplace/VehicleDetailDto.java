package com.paddock.api.dto.marketplace;

public record VehicleDetailDto(
        String make,
        String model,
        Integer year,
        String specClass
) {
}
