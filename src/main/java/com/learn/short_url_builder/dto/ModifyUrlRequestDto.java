package com.learn.short_url_builder.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

@Data
public class ModifyUrlRequestDto {

    @NonNull
    @NotEmpty
    private String shortUrl;

    @NonNull
    @NotEmpty
    private String newLongUrl;
}
