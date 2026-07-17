package com.codestorykh.knowledge.repository;

import com.codestorykh.knowledge.model.KnowledgeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends JpaRepository<KnowledgeEntry, Long> {

    List<KnowledgeEntry> findByCategoryAndIsActiveTrue(String category);

    List<KnowledgeEntry> findByIsActiveTrue();

    @Query("SELECT k FROM KnowledgeEntry k WHERE k.isActive = true AND " +
           "(LOWER(k.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(k.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(k.category) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<KnowledgeEntry> searchByQuery(@Param("query") String query);

    @Query("SELECT k FROM KnowledgeEntry k JOIN k.tags t WHERE t IN :tags AND k.isActive = true")
    List<KnowledgeEntry> findByTagsIn(@Param("tags") List<String> tags);

    @Query("SELECT DISTINCT k.category FROM KnowledgeEntry k WHERE k.isActive = true")
    List<String> findAllCategories();
}
