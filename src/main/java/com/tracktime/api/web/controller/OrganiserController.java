package com.tracktime.api.web.controller;

import com.tracktime.api.dto.OrganiserDto;
import com.tracktime.api.mapper.OrganiserMapper;
import com.tracktime.api.service.OrganiserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organisers")
@RequiredArgsConstructor
public class OrganiserController {

    private final OrganiserService organiserService;

    @GetMapping
    public ResponseEntity<List<OrganiserDto>> getAllOrganisers() {
        return ResponseEntity.ok(organiserService.getAllOrganisers().stream()
                .map(OrganiserMapper::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganiserDto> getOrganiserById(@PathVariable String id) {
        return organiserService.getOrganiserById(id)
                .map(OrganiserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}