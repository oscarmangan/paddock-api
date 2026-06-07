package com.tracktime.api.web.controller;

import com.tracktime.api.dto.OrganiserDto;
import com.tracktime.api.service.OrganiserService;
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

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(OrganiserController.class)
@AutoConfigureRestTestClient
class OrganiserControllerTest {

    @Autowired
    RestTestClient client;

    @MockitoBean
    OrganiserService organiserService;

    @Test
    @DisplayName("GET /api/organisers - returns list of all organisers")
    void getAllOrganisers_returnsList() {
        when(organiserService.getAllOrganisers()).thenReturn(List.of(msv(), javelin()));

        var result = client.get()
                .uri("/api/organisers")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<OrganiserDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(OrganiserDto::name)
                .containsExactly("MSV Track Days", "Javelin Trackdays");
    }

    @Test
    @DisplayName("GET /api/organisers/{id} - returns organiser when found")
    void getOrganiserById_returnsOrganiser() {
        when(organiserService.getOrganiserById("ORG_1")).thenReturn(msv());

        var result = client.get()
                .uri("/api/organisers/{id}", "ORG_1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrganiserDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("ORG_1");
        assertThat(result.name()).isEqualTo("MSV Track Days");
        assertThat(result.websiteUrl()).isEqualTo("https://www.msv.com");
    }

    @Test
    @DisplayName("GET /api/organisers/{id} - returns 404 when organiser not found")
    void getOrganiserById_notFound() {
        when(organiserService.getOrganiserById("ORG_999"))
                .thenThrow(new ResourceNotFoundException("Organiser", "ORG_999"));

        var result = client.get()
                .uri("/api/organisers/{id}", "ORG_999")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ApiError.class)
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo(404);
        assertThat(result.error()).isEqualTo("Not Found");
        assertThat(result.message()).isEqualTo("Organiser not found with id: ORG_999");
        assertThat(result.path()).isEqualTo("/api/organisers/ORG_999");
    }

    // --- fixtures ---

    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.parse("2026-01-01T00:00:00Z");

    private static OrganiserDto msv() {
        return new OrganiserDto(
                "ORG_1",
                "MSV Track Days",
                "https://www.msv.com",
                "/logos/msv.svg",
                FIXED_TIME
        );
    }

    private static OrganiserDto javelin() {
        return new OrganiserDto(
                "ORG_2",
                "Javelin Trackdays",
                "https://www.javelintrackdays.co.uk",
                "/logos/javelin.svg",
                FIXED_TIME
        );
    }
}