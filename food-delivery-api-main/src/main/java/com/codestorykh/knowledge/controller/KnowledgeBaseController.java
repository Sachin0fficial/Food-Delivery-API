package com.codestorykh.knowledge.controller;

import com.codestorykh.exception.ApiResponseUtil;
import com.codestorykh.knowledge.dto.KnowledgeRequest;
import com.codestorykh.knowledge.dto.KnowledgeSearchRequest;
import com.codestorykh.knowledge.service.KnowledgeBaseService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/knowledge")
@RequiredArgsConstructor
@Slf4j
public class KnowledgeBaseController {

    private final KnowledgeBaseService knowledgeBaseService;

    @GetMapping
    @Timed(value = "api.knowledge.list")
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(ApiResponseUtil.successResponse(knowledgeBaseService.getAll()));
    }

    @GetMapping("/{id}")
    @Timed(value = "api.knowledge.get")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseUtil.successResponse(knowledgeBaseService.getById(id)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Object> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResponseUtil.successResponse(knowledgeBaseService.getByCategory(category)));
    }

    @GetMapping("/categories")
    public ResponseEntity<Object> getCategories() {
        return ResponseEntity.ok(ApiResponseUtil.successResponse(knowledgeBaseService.getAllCategories()));
    }

    @PostMapping("/search")
    @Timed(value = "api.knowledge.search")
    public ResponseEntity<Object> search(@RequestBody KnowledgeSearchRequest request) {
        log.info("Knowledge search request: {}", request.getQuery());
        var results = knowledgeBaseService.search(request.getQuery());
        return ResponseEntity.ok(ApiResponseUtil.successResponse(results));
    }

    @PostMapping
    @Timed(value = "api.knowledge.create")
    public ResponseEntity<Object> create(@Valid @RequestBody KnowledgeRequest request) {
        log.info("Creating knowledge entry: {}", request.getTitle());
        var response = knowledgeBaseService.create(request);
        return new ResponseEntity<>(ApiResponseUtil.successResponse(response), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Timed(value = "api.knowledge.update")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody KnowledgeRequest request) {
        log.info("Updating knowledge entry: {}", id);
        var response = knowledgeBaseService.update(id, request);
        return ResponseEntity.ok(ApiResponseUtil.successResponse(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Deleting knowledge entry: {}", id);
        knowledgeBaseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export/okf")
    @Timed(value = "api.knowledge.export")
    public ResponseEntity<Object> exportOKF() {
        return ResponseEntity.ok(ApiResponseUtil.successResponse(knowledgeBaseService.exportAsOKF()));
    }
}
