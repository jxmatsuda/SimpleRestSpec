package com.example.simplerestspec.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "context_specs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContextSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String url;

    @OneToMany(mappedBy = "context", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ApiSpec> apiSpecs = new ArrayList<>();
}