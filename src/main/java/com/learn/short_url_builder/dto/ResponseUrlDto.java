package com.learn.short_url_builder.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUrlDto {
    private String shortUrl;
}
