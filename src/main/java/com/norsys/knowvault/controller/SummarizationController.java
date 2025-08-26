package com.norsys.knowvault.controller;

import com.norsys.knowvault.dto.SummarizeRequest;
import com.norsys.knowvault.dto.SummarizeResponse;
import com.norsys.knowvault.service.Impl.SummarizationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/ai", produces = MediaType.APPLICATION_JSON_VALUE)
public class SummarizationController {

    private final SummarizationService service;

    public SummarizationController(SummarizationService service) {
        this.service = service;
    }

    @PostMapping(path = "/summarize", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SummarizeResponse summarize(@RequestBody SummarizeRequest request) {
        return service.summarize(request);
    }
}
