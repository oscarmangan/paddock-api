package com.paddock.api.service;

import com.paddock.api.dto.OrganiserDto;
import com.paddock.api.mapper.OrganiserMapper;
import com.paddock.api.repository.OrganiserRepository;
import com.paddock.api.web.exception.ResourceNotFoundException;
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
