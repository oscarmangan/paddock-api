package com.paddock.api.web.controller;

import com.paddock.api.dto.marketplace.ListingDetailDto;
import com.paddock.api.dto.marketplace.ListingDto;
import com.paddock.api.dto.marketplace.ListingFilter;
import com.paddock.api.dto.shared.PagedResponse;
import com.paddock.api.service.ListingService;
import com.paddock.api.web.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @Operation(
            summary = "Get listing by ID",
            operationId = "getListingById"
    )
    @ApiResponse(responseCode = "200", description = "Listing found")
    @ApiResponse(
            responseCode = "404",
            description = "Listing not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
    )
    @GetMapping("/{id}")
    public ResponseEntity<ListingDetailDto> getListingById(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @Operation(
            summary = "Get listings",
            operationId = "getListings"
    )
    @ApiResponse(responseCode = "200", description = "Listings found matching criteria")
    @ApiResponse(
            responseCode = "400",
            description = "Invalid query parameter",
            content = @Content(schema = @Schema(implementation = ApiError.class))
    )
    @GetMapping
    public ResponseEntity<PagedResponse<ListingDto>> getListings(
            @RequestParam(required = false) String discipline,
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String county,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "newest") String sort) {
        ListingFilter filter = new ListingFilter(minPrice, maxPrice, county);
        return ResponseEntity.ok(listingService.getListings(discipline, categorySlug, filter, page, size, sort));
    }
}
