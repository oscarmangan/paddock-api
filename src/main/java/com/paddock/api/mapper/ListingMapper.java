package com.paddock.api.mapper;

import com.paddock.api.dto.marketplace.ListingDetailDto;
import com.paddock.api.dto.marketplace.ListingDto;
import com.paddock.api.dto.marketplace.VehicleDetailDto;
import com.paddock.api.model.marketplace.Listing;
import com.paddock.api.model.marketplace.ListingImage;
import com.paddock.api.model.marketplace.VehicleDetail;

public class ListingMapper {

    private ListingMapper() {}

    public static ListingDto toDto(Listing listing) {
        return new ListingDto(
                listing.getId(),
                listing.getDiscipline(),
                listing.getCategory().getName(),
                listing.getCategory().getSlug(),
                listing.getTitle(),
                listing.getSlug(),
                listing.getPrice(),
                listing.getPriceType().name(),
                listing.getCurrency(),
                listing.getTown(),
                listing.getCounty(),
                listing.getListingTier().name(),
                resolveThumbnail(listing),
                listing.getCreatedAt(),
                listing.getExpiresAt()
        );
    }

    public static ListingDetailDto toDetailDto(Listing listing) {
        return new ListingDetailDto(
                listing.getId(),
                listing.getDiscipline(),
                listing.getCategory().getName(),
                listing.getCategory().getSlug(),
                listing.getTitle(),
                listing.getSlug(),
                listing.getDescription(),
                listing.getPrice(),
                listing.getPriceType().name(),
                listing.getCurrency(),
                listing.isInclVat(),
                listing.getTown(),
                listing.getCounty(),
                listing.getListingTier().name(),
                listing.getImages().stream().map(ListingImage::getUrl).toList(),
                listing.getSeller().getFirstName() + " " + listing.getSeller().getLastName(),
                listing.getSeller().getPhoneNumber(),
                listing.getSeller().getCreatedAt(),
                toVehicleDetailDto(listing.getVehicleDetail()),
                listing.getCreatedAt(),
                listing.getExpiresAt()
        );
    }

    private static String resolveThumbnail(Listing listing) {
        return listing.getImages().stream()
                .filter(ListingImage::isThumbnail)
                .map(ListingImage::getUrl)
                .findFirst()
                .orElse(null);
    }

    private static VehicleDetailDto toVehicleDetailDto(VehicleDetail vehicleDetail) {
        if (vehicleDetail == null) return null;
        return new VehicleDetailDto(
                vehicleDetail.getMake(),
                vehicleDetail.getModel(),
                vehicleDetail.getYear() != null ? vehicleDetail.getYear().intValue() : null,
                vehicleDetail.getSpecClass()
        );
    }
}