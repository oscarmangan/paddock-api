package com.paddock.api.web.controller;

import com.paddock.api.dto.marketplace.CategoryDto;
import com.paddock.api.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WebMvcTest(CategoryController.class)
@AutoConfigureRestTestClient
class CategoryControllerTest {

    @Autowired
    RestTestClient client;

    @MockitoBean
    CategoryService categoryService;

    @Test
    @DisplayName("GET /api/categories/{discipline} - returns category tree for discipline")
    void getCategoriesByDiscipline_returnsTree() {
        when(categoryService.getCategoriesByDiscipline("rally")).thenReturn(rallyCategories());

        var result = client.get()
                .uri("/api/categories/{discipline}", "rally")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<CategoryDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull().hasSize(2);

        var cars = result.getFirst();
        assertThat(cars.slug()).isEqualTo("cars");
        assertThat(cars.discipline()).isEqualTo("rally");
        assertThat(cars.children()).hasSize(3);
        assertThat(cars.children().getFirst().slug()).isEqualTo("group-a");
        assertThat(cars.children().getFirst().children()).isEmpty();

        var parts = result.get(1);
        assertThat(parts.slug()).isEqualTo("parts-components");
        assertThat(parts.children()).isEmpty();
    }

    @Test
    @DisplayName("GET /api/categories/{discipline} - returns empty list for discipline with no categories")
    void getCategoriesByDiscipline_returnsEmptyList() {
        when(categoryService.getCategoriesByDiscipline("unknown")).thenReturn(List.of());

        var result = client.get()
                .uri("/api/categories/{discipline}", "unknown")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<CategoryDto>>() {})
                .returnResult()
                .getResponseBody();

        assertThat(result).isNotNull().isEmpty();
    }

    // --- Fixtures ---

    private static List<CategoryDto> rallyCategories() {
        var groupA = new CategoryDto(10L, "rally", "GROUP_A", "Group A", "group-a", 1L, 1, List.of());
        var groupN = new CategoryDto(11L, "rally", "GROUP_N", "Group N", "group-n", 1L, 2, List.of());
        var rc1rc3 = new CategoryDto(12L, "rally", "RC1_RC3", "RC1 → RC3", "rc1-rc3", 1L, 3, List.of());

        var cars = new CategoryDto(1L, "rally", "CARS", "Cars", "cars", null, 1, List.of(groupA, groupN, rc1rc3));
        var parts = new CategoryDto(2L, "rally", "PARTS_COMPONENTS", "Parts & Components", "parts-components", null, 2, List.of());

        return List.of(cars, parts);
    }
}