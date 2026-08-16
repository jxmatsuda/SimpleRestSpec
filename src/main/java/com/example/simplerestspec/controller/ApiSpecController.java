package com.example.simplerestspec.controller;

import com.example.simplerestspec.entity.ApiSpec;
import com.example.simplerestspec.repository.ApiSpecRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/specs")
@RequiredArgsConstructor
public class ApiSpecController {

    private final ApiSpecRepository repository;

    @GetMapping
    public List<ApiSpec> getAllSpecs() {
        return repository.findAll();
    }

    @GetMapping("/{contextId}")
    public ResponseEntity<List<ApiSpec>> getSpecsByContext(@PathVariable Long contextId) {
        return ResponseEntity.ok(repository.findByContext_Id(contextId));
    }

    @GetMapping("/{contextId}/{id}")
    public ResponseEntity<ApiSpec> getSpec(@PathVariable Long contextId, @PathVariable Long id) {
        return repository.findById(id)
                .filter(spec -> spec.getContext() != null && spec.getContext().getId().equals(contextId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSpec(@RequestBody ApiSpec spec) {
        if (spec.getContext() == null) {
            return ResponseEntity.badRequest().body("context_id is required");
        }
        try {
            log.info("Creating new ApiSpec: {}", spec);
            ApiSpec savedSpec = repository.save(spec);
            return ResponseEntity.ok(savedSpec);
        } catch (Exception e) {
            log.error("Error occurred while creating ApiSpec: ", e);
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{contextId}/{id}")
    public ResponseEntity<?> updateSpec(@PathVariable Long contextId, @PathVariable Long id, @RequestBody ApiSpec specDetails) {
        try {
            return repository.findById(id).filter(spec -> spec.getContext() != null && spec.getContext().getId().equals(contextId)).map(spec -> {
                spec.setName(specDetails.getName());
                spec.setPath(specDetails.getPath());
                spec.setMethod(specDetails.getMethod());
                spec.setPostType(specDetails.getPostType());
                
                // リストの更新は単なる set ではなく、内容をクリアして再設定することで orphanRemoval を正しく動作させる
                if (specDetails.getRequestParams() != null) {
                    spec.getRequestParams().clear();
                    spec.getRequestParams().addAll(specDetails.getRequestParams());
                }
                if (specDetails.getResponsePatterns() != null) {
                    spec.getResponsePatterns().clear();
                    spec.getResponsePatterns().addAll(specDetails.getResponsePatterns());
                }
                
                return ResponseEntity.ok(repository.save(spec));
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error occurred while updating ApiSpec: ", e);
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/{contextId}/{id}")
    public ResponseEntity<?> deleteSpec(@PathVariable Long contextId, @PathVariable Long id) {
        return repository.findById(id).filter(spec -> spec.getContext() != null && spec.getContext().getId().equals(contextId)).map(spec -> {
            repository.delete(spec);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}