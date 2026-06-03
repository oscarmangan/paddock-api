package com.tracktime.api.service;

import com.tracktime.api.model.Organiser;
import com.tracktime.api.repository.OrganiserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganiserService {

    private final OrganiserRepository organiserRepository;

    public List<Organiser> getAllOrganisers() {
        return organiserRepository.findAll();
    }

    public Optional<Organiser> getOrganiserById(String id) {
        return organiserRepository.findById(id);
    }

}
