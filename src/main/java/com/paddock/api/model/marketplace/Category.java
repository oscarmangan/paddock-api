package com.paddock.api.model.marketplace;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NaturalId
    @Column(name = "discipline", nullable = false, length = 20)
    private String discipline;

    @NaturalId
    @Column(name = "slug", nullable = false, length = 100)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<Category> children = new ArrayList<>();

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "label", nullable = false, length = 100)
    private String label;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

}
