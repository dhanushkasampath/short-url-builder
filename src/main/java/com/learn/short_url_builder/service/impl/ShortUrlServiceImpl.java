package com.learn.short_url_builder.service.impl;

import com.learn.short_url_builder.dto.GenerateUrlRequestDto;
import com.learn.short_url_builder.dto.RemoveUrlRequestDto;
import com.learn.short_url_builder.dto.ResponseUrlDto;
import com.learn.short_url_builder.repository.ShortUrlRepository;
import com.learn.short_url_builder.service.ShortUrlService;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    @Override
    public ResponseUrlDto generateShortUrl(GenerateUrlRequestDto generateUrlRequestDto) {
        ResponseUrlDto responseUrlDto = new ResponseUrlDto();
        responseUrlDto.setShortUrl("https://rb.gy/090byc");
        return responseUrlDto;
    }

    @Override
    public ResponseUrlDto modifyShortUrl() {
        ResponseUrlDto responseUrlDto = new ResponseUrlDto();
        responseUrlDto.setShortUrl("https://rb.gy/090byc");
        return responseUrlDto;
    }

    @Override
    public void removeShortUrl(RemoveUrlRequestDto removeUrlRequestDto) {

    }

    @Override
    public String queryByShortUrlId(String shortUrlId) {
        return "https://en.wikipedia.org/wiki/Sri_Lanka";
    }
}
