package com.paddock.api.service;

import com.paddock.api.dto.marketplace.ListingDetailDto;
import com.paddock.api.dto.marketplace.ListingDto;
import com.paddock.api.dto.marketplace.ListingFilter;
import com.paddock.api.dto.shared.PagedResponse;
import com.paddock.api.mapper.ListingMapper;
import com.paddock.api.model.marketplace.Listing;
import com.paddock.api.repository.ListingRepository;
import com.paddock.api.repository.specification.ListingSpecification;
import com.paddock.api.service.misc.ListingViewCounter;
import com.paddock.api.web.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final ListingViewCounter listingViewCounter;

    public ListingDetailDto getListingById(Long id) {
        Listing listing = listingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing", id.toString()));
        listingViewCounter.increment(id);
        return ListingMapper.toDetailDto(listing);
    }

    @Transactional(readOnly = true)
    public PagedResponse<ListingDto> getListings(String discipline, String categorySlug, ListingFilter filter,
                                                 int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, resolveSort(sort));
        Specification<Listing> spec = ListingSpecification.of(discipline, categorySlug, filter);
        Page<Listing> result = listingRepository.findAll(spec, pageable);
        return toPagedResponse(result);
    }

    @Async
    @Transactional
    public void incrementViewCountAsync(Long id) {
        listingRepository.incrementViewCount(id);
    }

    private Sort resolveSort(String sortOrder) {
        return switch (sortOrder) {
            case "price_asc"    -> Sort.by("price").ascending();
            case "price_desc"   -> Sort.by("price").descending();
            case "oldest"       -> Sort.by("createdAt").ascending();
            default             -> Sort.by("createdAt").descending();
        };
    }

    private PagedResponse<ListingDto> toPagedResponse(Page<Listing> page) {
        return new PagedResponse<>(
                page.getContent().stream().map(ListingMapper::toDto).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
