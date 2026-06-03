package com.tracktime.api.service;

import com.tracktime.api.dto.OrganiserDto;
import com.tracktime.api.mapper.OrganiserMapper;
import com.tracktime.api.repository.OrganiserRepository;
import com.tracktime.api.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganiserService {

    private final OrganiserRepository organiserRepository;

    public List<OrganiserDto> getAllOrganisers() {
        return organiserRepository.findAll().stream()
                .map(OrganiserMapper::toDto)
                .toList();
    }

    public OrganiserDto getOrganiserById(String id) {
        return organiserRepository.findById(id)
                .map(OrganiserMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Organiser", id));
    }

}
