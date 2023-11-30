package com.javalab.boot.domain.item;

import com.javalab.boot.domain.category.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemById(Long id);
    
    //추가
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select c from Category c where c.id =:id")
    Optional<Category> findByIdWithCategory(@Param("id") Long id);
}
