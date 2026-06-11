package com.paddock.api.service.misc;

import com.paddock.api.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListingViewCounter {

    private final ListingRepository listingRepository;

    @Async
    @Transactional
    public void increment(Long id) {
        listingRepository.incrementViewCount(id);
    }
}