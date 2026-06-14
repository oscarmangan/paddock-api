package com.paddock.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "track")
@Getter
@Setter
public class Track {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "region")
    private String region;

    @Column(name = "track_map_url")
    private String trackMapUrl;

    @Column(name = "locale")
    private String locale;

    @Column(name = "latitude", columnDefinition = "numeric(9,6)")
    private Double latitude;

    @Column(name = "longitude", columnDefinition = "numeric(9,6)")
    private Double longitude;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY)
    private List<TrackLayout> layouts;
}
