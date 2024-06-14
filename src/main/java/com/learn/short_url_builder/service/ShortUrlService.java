package com.learn.short_url_builder.service;

import com.learn.short_url_builder.dto.GenerateUrlRequestDto;
import com.learn.short_url_builder.dto.RemoveUrlRequestDto;
import com.learn.short_url_builder.dto.ResponseUrlDto;

public interface ShortUrlService {
    ResponseUrlDto generateShortUrl(GenerateUrlRequestDto generateUrlRequestDto);

    ResponseUrlDto modifyShortUrl();

    void removeShortUrl(RemoveUrlRequestDto removeUrlRequestDto);

    String queryByShortUrlId(String shortUrlId);
}
