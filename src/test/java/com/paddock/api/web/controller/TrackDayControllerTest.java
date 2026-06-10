package com.paddock.api.web.controller;

import com.paddock.api.dto.TrackDayDto;
import com.paddock.api.dto.shared.PagedResponse;
import com.paddock.api.service.TrackDayService;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(TrackDayController.class)
@AutoConfigureRestTestClient
class TrackDayControllerTest {

    @Autowired
    RestTestClient client;

    @MockitoBean
    TrackDayService trackDayService;

    @Test
    @DisplayName("GET /api/track-days/{id} - returns track day when found")
    void getTrackDayById_returnsTrackDay() {
        when(trackDayService.getTrackDayById(1L)).thenReturn(msvBrandsHatchTrackDay());

        var result = client.get()
                .uri("/api/track-days/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrackDayDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.trackName()).isEqualTo("Brands Hatch");
        assertThat(result.trackLayoutName()).isEqualTo("Indy");
        assertThat(result.organiserName()).isEqualTo("MSV Track Days");
        assertThat(result.noiseLimitStaticDecibels()).isEqualByComparingTo("105.0");
        assertThat(result.isSoldOut()).isFalse();
        assertThat(result.isVerified()).isTrue();
    }

    @Test
    @DisplayName("GET /api/track-days/{id} - returns 404 when track day not found")
    void getTrackDayById_returns404WhenNotFound() {
        when(trackDayService.getTrackDayById(999L))
                .thenThrow(new ResourceNotFoundException("TrackDay", "999"));

        var result = client.get()
                .uri("/api/track-days/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(404);
        assertThat(result.message()).isEqualTo("TrackDay not found with id: 999");
        assertThat(result.path()).isEqualTo("/api/track-days/999");
    }

    @Test
    @DisplayName("GET /api/track-days - returns paginated list of track days")
    void getAllTrackDays_returnsPagedResponse() {
        var pagedResponse = new PagedResponse<>(
                List.of(msvBrandsHatchTrackDay(), javelinSilverstoneTrackDay()),
                0,
                20,
                2L,
                1,
                true
        );

        when(trackDayService.getAllTrackDays(0, 20)).thenReturn(pagedResponse);

        var result = client.get()
                .uri("/api/track-days")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<TrackDayDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(2);
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(20);
        assertThat(result.totalElements()).isEqualTo(2L);
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.last()).isTrue();

        var first = result.content().getFirst();
        assertThat(first.trackName()).isEqualTo("Brands Hatch");
        assertThat(first.organiserName()).isEqualTo("MSV Track Days");

        var second = result.content().get(1);
        assertThat(second.trackName()).isEqualTo("Silverstone Circuit");
        assertThat(second.trackLayoutName()).isNull();
        assertThat(second.trackDayName()).isNull();
    }

    @Test
    @DisplayName("GET /api/track-days/track/{trackId} - returns paginated track days for a track")
    void getTrackDaysByTrack_returnsPagedResponse() {
        var pagedResponse = new PagedResponse<>(
                List.of(msvBrandsHatchTrackDay()),
                0,
                20,
                1L,
                1,
                true
        );

        when(trackDayService.getTrackDaysByTrackId("TR_3", 0, 20)).thenReturn(pagedResponse);

        var result = client.get()
                .uri("/api/track-days/track/{trackId}", "TR_3")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<TrackDayDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).trackId()).isEqualTo("TR_3");
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.last()).isTrue();
    }

    @Test
    @DisplayName("GET /api/track-days/organiser/{organiserId} - returns track days for an organiser")
    void getTrackDaysByOrganiser_returnsList() {
        when(trackDayService.getTrackDaysByOrganiserId("ORG_1"))
                .thenReturn(List.of(msvBrandsHatchTrackDay()));

        var result = client.get()
                .uri("/api/track-days/organiser/{organiserId}", "ORG_1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrackDayDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().organiserId()).isEqualTo("ORG_1");
        assertThat(result.getFirst().organiserName()).isEqualTo("MSV Track Days");
    }

    @Test
    @DisplayName("GET /api/track-days/track/{trackId}/range - returns track days within date range")
    void getTrackDaysByTrackAndDateRange_returnsList() {
        var from = OffsetDateTime.parse("2026-06-01T00:00:00Z");
        var to = OffsetDateTime.parse("2026-06-30T23:59:59Z");

        when(trackDayService.getTrackDaysByTrackAndDateRange("TR_3", from, to))
                .thenReturn(List.of(msvBrandsHatchTrackDay()));

        var result = client.get()
                .uri(u -> u
                        .path("/api/track-days/track/{trackId}/range")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build("TR_3"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrackDayDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().trackId()).isEqualTo("TR_3");
        assertThat(result.getFirst().startDatetime()).isEqualTo(FIXED_TIME);
    }

    @Test
    @DisplayName("GET /api/track-days/{id} - returns 400 when id is not a valid number")
    void getTrackDayById_returns400WhenIdIsInvalidType() {
        var result = client.get()
                .uri("/api/track-days/{id}", "NaN")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(400);
        assertThat(result.error()).isEqualTo("Bad Request");
        assertThat(result.message()).isEqualTo("Invalid value 'NaN' for parameter 'id'");
        assertThat(result.path()).isEqualTo("/api/track-days/NaN");
    }

    // --- Fixtures ---

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2026-06-01T09:00:00Z");

    private static TrackDayDto msvBrandsHatchTrackDay() {
        return new TrackDayDto(
                1L,
                "TR_3",
                "Brands Hatch",
                "TL_8",
                "Indy",
                "ORG_1",
                "MSV Track Days",
                "MSV Brands Hatch Track Day",
                FIXED_TIME,
                FIXED_TIME.plusHours(8),
                "https://www.msv.com/book/123",
                "OPEN_PITLANE",
                new BigDecimal("105.0"),
                new BigDecimal("92.5"),
                false,
                "scraped",
                FIXED_TIME,
                true,
                FIXED_TIME,
                FIXED_TIME
        );
    }

    private static TrackDayDto javelinSilverstoneTrackDay() {
        return new TrackDayDto(
                2L,
                "TR_8",
                "Silverstone Circuit",
                null,
                null,
                "ORG_2",
                "Javelin Trackdays",
                null,
                FIXED_TIME.plusDays(7),
                null,
                "https://www.javelin.com/book/456",
                null,
                new BigDecimal("98.0"),
                null,
                false,
                "scraped",
                FIXED_TIME,
                true,
                FIXED_TIME,
                FIXED_TIME
        );
    }

}
