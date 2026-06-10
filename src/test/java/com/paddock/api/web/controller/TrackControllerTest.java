package com.paddock.api.web.controller;

import com.paddock.api.dto.TrackDto;
import com.paddock.api.dto.TrackLayoutDto;
import com.paddock.api.service.TrackLayoutService;
import com.paddock.api.service.TrackService;
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
import static org.mockito.Mockito.when;

@WebMvcTest(TrackController.class)
@AutoConfigureRestTestClient
public class TrackControllerTest {

    @Autowired
    RestTestClient client;

    @MockitoBean
    TrackService trackService;

    @MockitoBean
    TrackLayoutService trackLayoutService;

    @Test
    @DisplayName("GET /api/tracks - returns list of all tracks with layouts")
    void getAllTracks_success() {
        var tracks = List.of(brandsHatch(), silverstone());

        when(trackService.getAllTracks()).thenReturn(tracks);

        var result = client.get()
                .uri("/api/tracks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrackDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(2);

        var brandsHatch = result.getFirst();
        assertThat(brandsHatch.id()).isEqualTo("TR_3");
        assertThat(brandsHatch.name()).isEqualTo("Brands Hatch");
        assertThat(brandsHatch.region()).isEqualTo("south");
        assertThat(brandsHatch.layouts()).hasSize(2);
        assertThat(brandsHatch.layouts().getFirst().id()).isEqualTo("TL_8");
        assertThat(brandsHatch.layouts().getFirst().name()).isEqualTo("Indy");

        var silverstone = result.get(1);
        assertThat(silverstone.id()).isEqualTo("TR_8");
        assertThat(silverstone.layouts()).isEmpty();
    }

    @Test
    @DisplayName("GET /api/tracks/{id} - returns track when found")
    void getTrackById_success() {
        when(trackService.getTrackById("TR_3")).thenReturn(brandsHatch());

        var result = client.get()
                .uri("/api/tracks/{id}", "TR_3")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TrackDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("TR_3");
        assertThat(result.name()).isEqualTo("Brands Hatch");
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.layouts()).hasSize(2);
    }

    @Test
    @DisplayName("GET /api/tracks/{id} - returns 404 when track not found")
    void getTrackById_notFound() {
        when(trackService.getTrackById("TR_999")).thenThrow(new ResourceNotFoundException("Track", "TR_999"));

        var result = client.get()
                .uri("/api/tracks/{id}", "TR_999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(404);
        assertThat(result.error()).isEqualTo("Not Found");
        assertThat(result.message()).isEqualTo("Track not found with id: TR_999");
        assertThat(result.path()).isEqualTo("/api/tracks/TR_999");
    }

    @Test
    @DisplayName("GET /api/tracks/region/{region} - returns tracks filtered by region")
    void getTracksByRegion_success() {
        when(trackService.getTracksByRegion("south")).thenReturn(List.of(brandsHatch()));

        var result = client.get()
                .uri("/api/tracks/region/{region}", "south")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrackDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(1);
        assertThat(result).extracting(TrackDto::region)
                .containsOnly("south");
    }

    @Test
    @DisplayName("GET /api/tracks/{id}/layouts - returns layouts for a track")
    void getLayoutsByTrack_success() {
        when(trackLayoutService.getLayoutsByTrackId("TR_3"))
                .thenReturn(List.of(brandsHatchIndy(), brandsHatchGP()));

        var result = client.get()
                .uri("/api/tracks/{id}/layouts", "TR_3")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TrackLayoutDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(TrackLayoutDto::name)
                .containsExactly("Indy", "Grand Prix");
    }

    // --- Fixtures ---

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2026-01-01T00:00:00Z");

    private static TrackLayoutDto brandsHatchIndy() {
        return new TrackLayoutDto("TL_8", "Indy", FIXED_TIME);
    }

    private static TrackLayoutDto brandsHatchGP() {
        return new TrackLayoutDto("TL_7", "Grand Prix", FIXED_TIME);
    }

    private static TrackDto brandsHatch() {
        return new TrackDto(
                "TR_3",
                "Brands Hatch",
                "south",
                "/maps/brands-hatch.svg",
                FIXED_TIME,
                List.of(brandsHatchIndy(), brandsHatchGP())
        );
    }

    private static TrackDto silverstone() {
        return new TrackDto(
                "TR_8",
                "Silverstone Circuit",
                "midlands",
                "/maps/silverstone.svg",
                FIXED_TIME,
                List.of()
        );
    }
}
