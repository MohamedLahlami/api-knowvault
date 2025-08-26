package com.norsys.knowvault.service.Impl;

import com.norsys.knowvault.dto.SummarizeRequest;
import com.norsys.knowvault.dto.SummarizeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SummarizationService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public SummarizationService(RestTemplate restTemplate,
                                @Value("${ai.summarizer.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public SummarizeResponse summarize(SummarizeRequest request) {
        ResponseEntity<SummarizeResponse> response = restTemplate.postForEntity(
                baseUrl + "/summarize",
                request,
                SummarizeResponse.class
        );
        return response.getBody();
    }
}
