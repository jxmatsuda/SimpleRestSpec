package com.example.simplerestspec.controller;

import com.example.simplerestspec.entity.ApiSpec;
import com.example.simplerestspec.entity.ContextSpec;
import com.example.simplerestspec.entity.RequestParamSpec;
import com.example.simplerestspec.entity.ResponsePatternSpec;
import com.example.simplerestspec.repository.ApiSpecRepository;
import com.example.simplerestspec.repository.ContextSpecRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.zip.*;

@Slf4j
@RestController
@RequestMapping("/api/contexts")
@RequiredArgsConstructor
public class ContextSpecController {

    private final ContextSpecRepository repository;
    private final ApiSpecRepository apiSpecRepository;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<ContextSpec> getAllContexts() {
        List<ContextSpec> contexts = repository.findAll();
        contexts.sort(Comparator.comparing(ContextSpec::getUrl,String.CASE_INSENSITIVE_ORDER));
        return contexts;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContextSpec> getContext(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createContext(@RequestBody ContextSpec context) {
        try {
            log.info("Creating new ContextSpec: {}", context);
            ContextSpec savedContext = repository.save(context);
            return ResponseEntity.ok(savedContext);
        } catch (Exception e) {
            log.error("Error occurred while creating ContextSpec: ", e);
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContextSpec> updateContext(@PathVariable Long id, @RequestBody ContextSpec contextDetails) {
        return repository.findById(id).map(context -> {
            context.setUrl(contextDetails.getUrl());
            context.setMemo(contextDetails.getMemo());
            return ResponseEntity.ok(repository.save(context));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContext(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<Resource> exportContext(@PathVariable Long id) {
        try {
            ContextSpec context = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Context not found"));
            List<ApiSpec> specs = apiSpecRepository.findByContext_Id(id);

            ContextSpec exportContext = ContextSpec.builder()
                    .id(context.getId())
                    .url(context.getUrl())
                    .apiSpecs(new ArrayList<>())
                    .build();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                // Context info
                ZipEntry contextEntry = new ZipEntry("context.json");
                zos.putNextEntry(contextEntry);
                zos.write(objectMapper.writeValueAsBytes(exportContext));
                zos.closeEntry();

                // Specs info
                ZipEntry specsEntry = new ZipEntry("specs.json");
                zos.putNextEntry(specsEntry);
                zos.write(objectMapper.writeValueAsBytes(specs));
                zos.closeEntry();
            }

            byte[] zipBytes = baos.toByteArray();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"context_" + id + ".zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayResource(zipBytes));
        } catch (Exception e) {
            log.error("Export failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/import")
    public ResponseEntity<?> importContext(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ContextSpec importedContext = null;
            List<ApiSpec> importedSpecs = null;

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    byte[] bytes = zis.readAllBytes();
                    if ("context.json".equals(entry.getName())) {
                        importedContext = objectMapper.readValue(bytes, ContextSpec.class);
                    } else if ("specs.json".equals(entry.getName())) {
                        importedSpecs = objectMapper.readValue(bytes, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, ApiSpec.class));
                    }
                }
            }

            if (importedContext == null) {
                return ResponseEntity.badRequest().body("Missing context.json in zip file");
            }

            // IDをリセットして新規保存
            importedContext.setId(null);
            ContextSpec savedContext = repository.save(importedContext);

            if (importedSpecs != null) {
                for (ApiSpec spec : importedSpecs) {
                    spec.setId(null);
                    spec.setContext(savedContext);
                    if (spec.getRequestParams() != null) {
                        for (RequestParamSpec param : spec.getRequestParams()) {
                            param.setId(null);
                        }
                    }
                    if (spec.getResponsePatterns() != null) {
                        for (ResponsePatternSpec pattern : spec.getResponsePatterns()) {
                            pattern.setId(null);
                        }
                    }
                    apiSpecRepository.save(spec);
                }
            }

            return ResponseEntity.ok(savedContext);
        } catch (Exception e) {
            log.error("Import failed", e);
            return ResponseEntity.internalServerError().body("Import failed: " + e.getMessage());
        }
    }
}
