package com.example.simplerestspec.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "response_pattern_specs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePatternSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String patternName;
    private Integer statusCode;
    private String responseType; // 'JSON' or 'BINARY'
    
    @Column(columnDefinition = "TEXT")
    private String bodyJson;
}