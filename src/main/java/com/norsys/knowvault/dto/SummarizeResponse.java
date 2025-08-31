package com.norsys.knowvault.dto;

import lombok.Data;

@Data
public class SummarizeResponse {
    private String summary;
    private int sentencesReturned;
    private String algorithm;
    private String language;
}
