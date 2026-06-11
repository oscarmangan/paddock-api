package com.paddock.api.repository;

import com.paddock.api.model.marketplace.Category;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@NullMarked
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            SELECT c FROM Category c
                LEFT JOIN FETCH c.children
            WHERE c.discipline = :discipline
            AND c.parent IS NULL
            ORDER BY c.displayOrder ASC
        """)
    List<Category> findTopLevelWithChildrenByDiscipline(String discipline);

    Optional<Category> findByDisciplineAndSlug(String discipline, String slug);
}