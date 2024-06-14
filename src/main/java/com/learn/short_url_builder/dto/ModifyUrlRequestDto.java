package com.learn.short_url_builder.dto;

import lombok.Data;

@Data
public class ModifyUrlRequestDto {
    private String shortUrl;
    private String newLongUrl;
}
