package com.codestorykh.knowledge.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "tbl_knowledge_base")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String category;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tbl_knowledge_tags", joinColumns = @JoinColumn(name = "knowledge_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "schema_type")
    private String schemaType;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "source_url")
    private String sourceUrl;

    private String author;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
