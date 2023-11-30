package com.javalab.boot.domain.category;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.javalab.boot.domain.category.Category;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select c from Category c where c.id =:id")
    Optional<Category> findByIdWithImages(@Param("id") Long id);


}
