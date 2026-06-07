package com.tracktime.api.web.controller;

import com.tracktime.api.dto.EventDto;
import com.tracktime.api.dto.shared.PagedResponse;
import com.tracktime.api.service.EventService;
import com.tracktime.api.web.exception.ApiError;
import com.tracktime.api.web.exception.ResourceNotFoundException;
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

@WebMvcTest(EventController.class)
@AutoConfigureRestTestClient
class EventControllerTest {

    @Autowired
    RestTestClient client;

    @MockitoBean
    EventService eventService;

    @Test
    @DisplayName("GET /api/events/{id} - returns event when found")
    void getEventById_returnsEvent() {
        when(eventService.getEventById(1L)).thenReturn(msvBrandsHatchEvent());

        var result = client.get()
                .uri("/api/events/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EventDto.class)
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
    @DisplayName("GET /api/events/{id} - returns 404 when event not found")
    void getEventById_returns404WhenNotFound() {
        when(eventService.getEventById(999L))
                .thenThrow(new ResourceNotFoundException("Event", "999"));

        var result = client.get()
                .uri("/api/events/{id}", 999L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(404);
        assertThat(result.message()).isEqualTo("Event not found with id: 999");
        assertThat(result.path()).isEqualTo("/api/events/999");
    }

    @Test
    @DisplayName("GET /api/events - returns paginated list of events")
    void getAllEvents_returnsPagedResponse() {
        var pagedResponse = new PagedResponse<>(
                List.of(msvBrandsHatchEvent(), javelinSilverstoneEvent()),
                0,
                20,
                2L,
                1,
                true
        );

        when(eventService.getAllEvents(0, 20)).thenReturn(pagedResponse);

        var result = client.get()
                .uri("/api/events")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<EventDto>>() {})
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
        assertThat(second.eventName()).isNull();
    }

    @Test
    @DisplayName("GET /api/events/track/{trackId} - returns paginated events for a track")
    void getEventsByTrack_returnsPagedResponse() {
        var pagedResponse = new PagedResponse<>(
                List.of(msvBrandsHatchEvent()),
                0,
                20,
                1L,
                1,
                true
        );

        when(eventService.getEventsByTrackId("TR_3", 0, 20)).thenReturn(pagedResponse);

        var result = client.get()
                .uri("/api/events/track/{trackId}", "TR_3")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<PagedResponse<EventDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).trackId()).isEqualTo("TR_3");
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.last()).isTrue();
    }

    @Test
    @DisplayName("GET /api/events/organiser/{organiserId} - returns events for an organiser")
    void getEventsByOrganiser_returnsList() {
        when(eventService.getEventsByOrganiserId("ORG_1"))
                .thenReturn(List.of(msvBrandsHatchEvent()));

        var result = client.get()
                .uri("/api/events/organiser/{organiserId}", "ORG_1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<EventDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().organiserId()).isEqualTo("ORG_1");
        assertThat(result.getFirst().organiserName()).isEqualTo("MSV Track Days");
    }

    @Test
    @DisplayName("GET /api/events/track/{trackId}/range - returns events within date range")
    void getEventsByTrackAndDateRange_returnsList() {
        var from = OffsetDateTime.parse("2026-06-01T00:00:00Z");
        var to = OffsetDateTime.parse("2026-06-30T23:59:59Z");

        when(eventService.getEventsByTrackAndDateRange("TR_3", from, to))
                .thenReturn(List.of(msvBrandsHatchEvent()));

        var result = client.get()
                .uri(u -> u
                        .path("/api/events/track/{trackId}/range")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build("TR_3"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<EventDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().trackId()).isEqualTo("TR_3");
        assertThat(result.getFirst().startDatetime()).isEqualTo(FIXED_TIME);
    }

    @Test
    @DisplayName("GET /api/events/{id} - returns 400 when id is not a valid number")
    void getEventById_returns400WhenIdIsInvalidType() {
        var result = client.get()
                .uri("/api/events/{id}", "NaN")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(400);
        assertThat(result.error()).isEqualTo("Bad Request");
        assertThat(result.message()).isEqualTo("Invalid value 'NaN' for parameter 'id'");
        assertThat(result.path()).isEqualTo("/api/events/NaN");
    }

    // --- Fixtures ---

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2026-06-01T09:00:00Z");

    private static EventDto msvBrandsHatchEvent() {
        return new EventDto(
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

    private static EventDto javelinSilverstoneEvent() {
        return new EventDto(
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