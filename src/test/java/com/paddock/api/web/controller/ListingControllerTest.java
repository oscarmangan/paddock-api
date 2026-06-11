package com.paddock.api.web.controller;

import com.paddock.api.dto.marketplace.ListingDetailDto;
import com.paddock.api.dto.marketplace.ListingDto;
import com.paddock.api.dto.marketplace.ListingFilter;
import com.paddock.api.dto.marketplace.VehicleDetailDto;
import com.paddock.api.dto.shared.PagedResponse;
import com.paddock.api.service.ListingService;
import com.paddock.api.web.exception.ApiError;
import com.paddock.api.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ListingController.class)
@AutoConfigureRestTestClient
class ListingControllerTest {

    @Autowired
    RestTestClient client;

    @MockitoBean
    ListingService listingService;

    @Test
    @DisplayName("GET /api/listings/{id} - returns listing detail when found")
    void getListingById_returnsDetail() {
        when(listingService.getListingById(1L)).thenReturn(groupASubaruDetail());

        var result = client.get()
                .uri("/api/listings/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ListingDetailDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.discipline()).isEqualTo("rally");
        assertThat(result.categorySlug()).isEqualTo("group-a");
        assertThat(result.title()).isEqualTo("Subaru Impreza WRC S12 2004");
        assertThat(result.price()).isEqualTo(2500000);
        assertThat(result.priceType()).isEqualTo("FIXED");
        assertThat(result.town()).isEqualTo("Builth Wells");
        assertThat(result.county()).isEqualTo("Powys");
        assertThat(result.sellerDisplayName()).isEqualTo("John Smith");
        assertThat(result.vehicleDetail()).isNotNull();
        assertThat(result.vehicleDetail().make()).isEqualTo("Subaru");
        assertThat(result.vehicleDetail().specClass()).isEqualTo("Group A");
        assertThat(result.imageUrls()).hasSize(2);
    }

    @Test
    @DisplayName("GET /api/listings/{id} - returns 404 when listing not found")
    void getListingById_returns404WhenNotFound() {
        when(listingService.getListingById(999L))
                .thenThrow(new ResourceNotFoundException("Listing", "999"));

        var result = client.get()
                .uri("/api/listings/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(404);
        assertThat(result.message()).isEqualTo("Listing not found with id: 999");
        assertThat(result.path()).isEqualTo("/api/listings/999");
    }

    @Test
    @DisplayName("GET /api/listings/{id} - returns 400 when id is not a valid number")
    void getListingById_returns400WhenIdIsInvalidType() {
        var result = client.get()
                .uri("/api/listings/{id}", "NaN")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(400);
        assertThat(result.message()).isEqualTo("Invalid value 'NaN' for parameter 'id'");
        assertThat(result.path()).isEqualTo("/api/listings/NaN");
    }

    @Test
    @DisplayName("GET /api/listings - returns paginated listings")
    void getListings_returnsPagedResponse() {
        var pagedResponse = new PagedResponse<>(
                List.of(groupASubaruCard(), formulaFordCard()),
                0,
                20,
                2L,
                1,
                true
        );

        when(listingService.getListings(isNull(), isNull(), any(ListingFilter.class),
                eq(0), eq(20), eq("newest")))
                .thenReturn(pagedResponse);

        var result = client.get()
                .uri("/api/listings")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<ListingDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(20);
        assertThat(result.totalElements()).isEqualTo(2L);
        assertThat(result.last()).isTrue();
    }

    @Test
    @DisplayName("GET /api/listings - filters by discipline and categorySlug")
    void getListings_filtersByDisciplineAndCategory() {
        var pagedResponse = new PagedResponse<>(
                List.of(groupASubaruCard()),
                0,
                20,
                1L,
                1,
                true
        );

        when(listingService.getListings(eq("rally"), eq("group-a"), any(ListingFilter.class),
                eq(0), eq(20), eq("newest")))
                .thenReturn(pagedResponse);

        var result = client.get()
                .uri(u -> u.path("/api/listings")
                        .queryParam("discipline", "rally")
                        .queryParam("categorySlug", "group-a")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<ListingDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.content().getFirst().discipline()).isEqualTo("rally");
        assertThat(result.content().getFirst().categorySlug()).isEqualTo("group-a");
    }

    @Test
    @DisplayName("GET /api/listings - filters by price range")
    void getListings_filtersByPriceRange() {
        var pagedResponse = new PagedResponse<>(
                List.of(groupASubaruCard()),
                0,
                20,
                1L,
                1,
                true
        );

        when(listingService.getListings(eq(null), eq(null), any(ListingFilter.class), eq(0), eq(20), eq("newest")))
                .thenReturn(pagedResponse);

        var result = client.get()
                .uri(u -> u.path("/api/listings")
                        .queryParam("minPrice", 1000000)
                        .queryParam("maxPrice", 3000000)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<ListingDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
    }

    @Test
    @DisplayName("GET /api/listings - returns empty page when no listings match")
    void getListings_returnsEmptyPage() {
        var pagedResponse = new PagedResponse<ListingDto>(
                List.of(),
                0,
                20,
                0L,
                0,
                true
        );

        when(listingService.getListings(eq("kart"), eq(null), any(ListingFilter.class), eq(0), eq(20), eq("newest")))
                .thenReturn(pagedResponse);

        var result = client.get()
                .uri(u -> u.path("/api/listings")
                        .queryParam("discipline", "kart")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<ListingDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }

    // --- Fixtures ---

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2026-06-01T09:00:00Z");

    private static ListingDetailDto groupASubaruDetail() {
        return new ListingDetailDto(
                1L,
                "rally",
                "Group A",
                "group-a",
                "Subaru Impreza WRC S12 2004",
                "subaru-impreza-wrc-s12-2004",
                "Full Group A specification Subaru Impreza. Ready to compete.",
                2500000,
                "FIXED",
                "GBP",
                false,
                "Builth Wells",
                "Powys",
                "STANDARD",
                List.of(
                        "https://cdn.paddock.co.uk/listings/1/img1.jpg",
                        "https://cdn.paddock.co.uk/listings/1/img2.jpg"
                ),
                "John Smith",
                "+447700900123",
                FIXED_TIME.minusYears(2),
                new VehicleDetailDto("Subaru", "Impreza", 2004, "Group A"),
                FIXED_TIME,
                FIXED_TIME.plusDays(90)
        );
    }

    private static ListingDto groupASubaruCard() {
        return new ListingDto(
                1L,
                "rally",
                "Group A",
                "group-a",
                "Subaru Impreza WRC S12 2004",
                "subaru-impreza-wrc-s12-2004",
                2500000,
                "FIXED",
                "GBP",
                "Builth Wells",
                "Powys",
                "STANDARD",
                "https://cdn.paddock.co.uk/listings/1/img1.jpg",
                FIXED_TIME,
                FIXED_TIME.plusDays(90)
        );
    }

    private static ListingDto formulaFordCard() {
        return new ListingDto(
                2L,
                "race",
                "Formula / Single Seater",
                "formula-single-seater",
                "Van Diemen RF00 Formula Ford",
                "van-diemen-rf00-formula-ford",
                450000,
                "FIXED",
                "GBP",
                "Huntingdon",
                "Cambridgeshire",
                "FEATURED",
                "https://cdn.paddock.co.uk/listings/2/img1.jpg",
                FIXED_TIME.plusDays(1),
                FIXED_TIME.plusDays(91)
        );
    }
}