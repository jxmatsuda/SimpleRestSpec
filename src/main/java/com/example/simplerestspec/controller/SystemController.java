package com.example.simplerestspec.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Data
    @AllArgsConstructor
    public static class EndpointInfo {
        private String method;
        private String endpoint;
        private String description;
        private String example;
    }

    @GetMapping("/endpoints")
    public List<EndpointInfo> getEndpoints() {
        String contextExample = "{\"id\": 1, \"url\": \"http://localhost:8080/SimpleRestSpec\"}";
        String apiSpecExample = "{" +
                "\"id\": 1," +
                "\"context_id\": 1," +
                "\"name\": \"Get User API\"," +
                "\"path\": \"/api/users\", " +
                "\"method\": \"GET\"," +
                "\"postType\": \"JSON\"," +
                "\"requestParams\": [{\"id\": 1, \"paramType\": \"QUERY\", \"name\": \"id\", \"dataType\": \"number\", \"required\": true, \"description\": \"User ID\"}]," +
                "\"responsePatterns\": [{\"id\": 1, \"patternName\": \"Success\", \"statusCode\": 200, \"responseType\": \"JSON\", \"bodyJson\": \"{\\\"id\\\": 1, \\\"name\\\": \\\"John Doe\\\"}\"}]" +
                "}";

        return Arrays.asList(
            // ContextSpec APIs
            new EndpointInfo("GET", "/api/contexts", "Get all contexts", "[" + contextExample + "]"),
            new EndpointInfo("GET", "/api/contexts/{id}", "Get a specific context by ID", contextExample),
            new EndpointInfo("POST", "/api/contexts", "Create a new context", contextExample),
            new EndpointInfo("PUT", "/api/contexts/{id}", "Update an existing context", contextExample),
            new EndpointInfo("DELETE", "/api/contexts/{id}", "Delete a specific context", null),
            new EndpointInfo("GET", "/api/contexts/{id}/export", "Export context and its specs as ZIP", "Binary ZIP file"),
            new EndpointInfo("POST", "/api/contexts/import", "Import context and specs from ZIP", "Multipart file upload"),
            
            // ApiSpec APIs
            new EndpointInfo("GET", "/api/specs", "Get all API specifications", "[" + apiSpecExample + "]"),
            new EndpointInfo("GET", "/api/specs/{contextId}", "Get all API specifications for a specific context", "[" + apiSpecExample + "]"),
            new EndpointInfo("GET", "/api/specs/{contextId}/{id}", "Get a specific API specification by ID", apiSpecExample),
            new EndpointInfo("POST", "/api/specs", "Create a new API specification", apiSpecExample),
            new EndpointInfo("PUT", "/api/specs/{contextId}/{id}", "Update an existing API specification", apiSpecExample),
            new EndpointInfo("DELETE", "/api/specs/{contextId}/{id}", "Delete a specific API specification", null)
        );
    }
}