package com.learn.short_url_builder.service;

import com.learn.short_url_builder.dto.GenerateUrlRequestDto;
import com.learn.short_url_builder.dto.ModifyUrlRequestDto;
import com.learn.short_url_builder.dto.RemoveUrlRequestDto;
import com.learn.short_url_builder.dto.ResponseUrlDto;

import java.net.UnknownHostException;

public interface ShortUrlService {
    ResponseUrlDto generateShortUrl(GenerateUrlRequestDto generateUrlRequestDto) throws UnknownHostException;

    String modifyShortUrl(ModifyUrlRequestDto modifyUrlRequestDto) throws UnknownHostException;

    void removeShortUrl(RemoveUrlRequestDto removeUrlRequestDto);

    String queryByShortUrlId(String shortUrlId);
}
