package com.paddock.api.model.marketplace;

import com.paddock.api.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "listing")
@Getter
@Setter
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "discipline", nullable = false, length = 20)
    private String discipline;

    @Column(name = "title", nullable = false, length = 60)
    private String title;

    @Column(name = "slug", nullable = false, length = 80)
    private String slug;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_type", nullable = false, length = 10)
    private PriceType priceType;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "incl_vat", nullable = false)
    private boolean inclVat;

    @Column(name = "town", nullable = false, length = 100)
    private String town;

    @Column(name = "county", nullable = false, length = 100)
    private String county;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "listing_tier", nullable = false, length = 10)
    private ListingTier listingTier;

    @Column(name = "bump_count", nullable = false)
    private int bumpCount;

    @Column(name = "last_bumped_at")
    private OffsetDateTime lastBumpedAt;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "report_count", nullable = false)
    private int reportCount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "extended_at")
    private OffsetDateTime extendedAt;

    @Column(name = "sold_at")
    private OffsetDateTime soldAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<ListingImage> images = new ArrayList<>();

    @OneToOne(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private VehicleDetail vehicleDetail;

    public enum Status {
        PENDING, ACTIVE, EXPIRED, SOLD, REMOVED
    }

    public enum PriceType {
        FIXED, POA, FREE, WANTED
    }

    public enum ListingTier {
        STANDARD, FEATURED
    }
}
