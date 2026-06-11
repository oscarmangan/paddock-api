package com.paddock.api.repository;

import com.paddock.api.model.marketplace.Listing;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NullMarked
public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {

    @Query("""
            SELECT l from Listing l
                JOIN FETCH l.category
                JOIN FETCH l.seller
            WHERE l.id = :id
            AND l.status = 'ACTIVE'
            """)
    Optional<Listing> findActiveById(Long id);

    @Modifying
    @Query("UPDATE Listing l SET l.viewCount = l.viewCount + 1 WHERE l.id = :id")
    void incrementViewCount(Long id);

}
