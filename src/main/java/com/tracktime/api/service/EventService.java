package com.tracktime.api.service;

import com.tracktime.api.model.Event;
import com.tracktime.api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventsByTrackId(String trackId) {
        return eventRepository.findByTrackId(trackId);
    }

    public List<Event> getEventsByOrganiserId(String organiserId) {
        return eventRepository.findByOrganiserId(organiserId);
    }

    public List<Event> getEventsByDateRange(OffsetDateTime from, OffsetDateTime to) {
        return eventRepository.findByStartDatetimeBetween(from, to);
    }

    public List<Event> getEventsByTrackAndDateRange(String trackId, OffsetDateTime from, OffsetDateTime to) {
        return eventRepository.findByTrackIdAndStartDatetimeBetween(trackId, from, to);
    }

    public Optional<Event> getEventByBookingUrl(String bookingUrl) {
        return eventRepository.findByBookingUrl(bookingUrl);
    }
}