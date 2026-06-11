package com.paddock.api.repository.specification;

import com.paddock.api.dto.marketplace.ListingFilter;
import com.paddock.api.model.marketplace.Listing;
import org.springframework.data.jpa.domain.Specification;

public class ListingSpecification {

    private ListingSpecification() {}

    public static Specification<Listing> of(String discipline, String categorySlug, ListingFilter filter) {
        Specification<Listing> spec = active();

        if (discipline != null && !discipline.isBlank()) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(root.get("discipline"), discipline));
        }
        if (categorySlug != null && !categorySlug.isBlank()) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(root.get("category").get("slug"), categorySlug));
        }
        if (filter.minPrice() != null) {
            spec = spec.and((root, query, cb)
                    -> cb.greaterThanOrEqualTo(root.get("price"), filter.minPrice()));
        }
        if (filter.maxPrice() != null) {
            spec = spec.and((root, query, cb)
                    -> cb.lessThanOrEqualTo(root.get("price"), filter.maxPrice()));
        }
        if (filter.county() != null && !filter.county().isBlank()) {
            spec = spec.and((root, query, cb)
                    -> cb.equal(cb.lower(root.get("county")), filter.county().toLowerCase()));
        }

        return spec;
    }

    private static Specification<Listing> active() {
        return (root, query, cb) -> cb.equal(root.get("status"), Listing.Status.ACTIVE);
    }

}
