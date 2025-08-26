package com.norsys.knowvault.dto;

import lombok.Data;

@Data
public class SummarizeRequest {
    private String document;
    private String language;
    private Integer sentences;
    private String algorithm;
}
