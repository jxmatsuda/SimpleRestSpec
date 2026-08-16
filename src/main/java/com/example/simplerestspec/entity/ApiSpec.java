package com.example.simplerestspec.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "api_specs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "context_id")
    @JsonIgnore
    private ContextSpec context;

    @JsonProperty("context_id")
    public void setContextId(Long contextId) {
        if (contextId != null) {
            this.context = ContextSpec.builder().id(contextId).build();
        }
    }

    @JsonProperty("context_id")
    public Long getContextId() {
        return context != null ? context.getId() : null;
    }

    private String name;
    private String path;
    private String method;
    private String postType;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "api_spec_id")
    @Builder.Default
    private List<RequestParamSpec> requestParams = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "api_spec_id")
    @Builder.Default
    private List<ResponsePatternSpec> responsePatterns = new ArrayList<>();
}