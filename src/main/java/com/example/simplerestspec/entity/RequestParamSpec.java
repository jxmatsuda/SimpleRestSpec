package com.example.simplerestspec.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "request_param_specs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String paramType; // 'PATH', 'QUERY', 'BODY', 'FILE'
    private String name;
    private String dataType;
    private Boolean required;
    private String description;
}