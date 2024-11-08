package com.learn.short_url_builder.service.impl;

import com.learn.short_url_builder.dto.GenerateUrlRequestDto;
import com.learn.short_url_builder.dto.ModifyUrlRequestDto;
import com.learn.short_url_builder.dto.RemoveUrlRequestDto;
import com.learn.short_url_builder.dto.ResponseUrlDto;
import com.learn.short_url_builder.entity.ShortUrlData;
import com.learn.short_url_builder.exception.ShortUrlBuilderException;
import com.learn.short_url_builder.repository.ShortUrlRepository;
import com.learn.short_url_builder.service.ShortUrlService;
import com.learn.short_url_builder.util.RandomStringGenerator;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortUrlServiceImpl implements ShortUrlService {

    public static final String INVALID_SHORT_URL = "Invalid Short Url.";
    private final ShortUrlRepository shortUrlRepository;

    private final RandomStringGenerator randomStringGenerator;

    @Value("${server.port}")
    private int port;

    @Value("${random}")
    private int length;

    @Value("${link.expires.in}")
    private int noOfDaysToExpire;

    @Override
    @RateLimiter(name = "myServiceRateLimiter", fallbackMethod = "rateLimitFallbackForGenerate")
    public ResponseUrlDto generateShortUrl(GenerateUrlRequestDto generateUrlRequestDto) throws UnknownHostException {

        String uniqueString = randomStringGenerator.generateRandomString(length);
        ShortUrlData shortUrlDataDocument = ShortUrlData.builder()
                .longUrl(generateUrlRequestDto.getLongUrl())
                .shortUrl(uniqueString)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(noOfDaysToExpire))
                .isDeleted(false)
                .build();

        shortUrlRepository.save(shortUrlDataDocument);

        return ResponseUrlDto.builder().shortUrl(generateRandomUrl(uniqueString)).build();
    }

    /**
     * we can use this method when we need the whole dynamic url
     * @param uniqueString
     * @return
     * @throws UnknownHostException
     */
    private String generateRandomUrl(String uniqueString) throws UnknownHostException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append(InetAddress.getLocalHost().getHostAddress());
        stringBuilder.append(":");
        stringBuilder.append(Integer.toString(this.port));
        stringBuilder.append("/");
        stringBuilder.append(uniqueString);
        return stringBuilder.toString();
    }

    @Override
    @RateLimiter(name = "myServiceRateLimiter", fallbackMethod = "rateLimitFallbackForModify")
    public ResponseUrlDto modifyShortUrl(ModifyUrlRequestDto modifyUrlRequestDto) throws UnknownHostException {
        Optional<ShortUrlData> shortUrlDataOptional = shortUrlRepository.findByShortUrlAndValidExpiry(modifyUrlRequestDto.getShortUrl());
        if(shortUrlDataOptional.isEmpty()){
            throw new ShortUrlBuilderException(INVALID_SHORT_URL);
        }
        ShortUrlData shortUrlData = shortUrlDataOptional.get();
        shortUrlData.setLongUrl(modifyUrlRequestDto.getNewLongUrl());
        shortUrlData.setLastModifiedDate(LocalDateTime.now());
        shortUrlData.setExpiryDate(LocalDateTime.now().plusDays(noOfDaysToExpire));
        shortUrlRepository.save(shortUrlData);

        return ResponseUrlDto.builder().shortUrl(generateRandomUrl(modifyUrlRequestDto.getShortUrl())).build();
    }

    @Override
    @RateLimiter(name = "myServiceRateLimiter", fallbackMethod = "rateLimitFallbackForRemoval")
    public void removeShortUrl(RemoveUrlRequestDto removeUrlRequestDto) {
        Optional<ShortUrlData> shortUrlDataOptional = shortUrlRepository.findByShortUrlAndValidExpiry(removeUrlRequestDto.getShortUrl());
        if(shortUrlDataOptional.isEmpty()){
            throw new ShortUrlBuilderException(INVALID_SHORT_URL);
        }
        ShortUrlData shortUrlData = shortUrlDataOptional.get();
        shortUrlData.setIsDeleted(true);
        shortUrlRepository.save(shortUrlData);
    }

    @Override
    @Cacheable(cacheNames = "query-short-url", key = "#shortUrlId")
    @RateLimiter(name = "myServiceRateLimiter", fallbackMethod = "rateLimitFallbackForQuery")
    public String queryByShortUrlId(String shortUrlId) {
        log.info("Service layer received the request | to queryByShortUrlId");
        Optional<ShortUrlData> shortUrlData = shortUrlRepository.findByShortUrlAndValidExpiry(shortUrlId);
        if(shortUrlData.isEmpty()){
            throw new ShortUrlBuilderException(INVALID_SHORT_URL);
        }
        return shortUrlData.get().getLongUrl();
    }

    public String rateLimitFallbackForQuery(Throwable t) {
        return "Rate limit exceeded. Please try again later.";
    }

    public ResponseUrlDto rateLimitFallbackForGenerate(Throwable t) {
        return ResponseUrlDto.builder().shortUrl("Rate limit exceeded. Please try again later.").build();
    }

    public void rateLimitFallbackForRemoval(Throwable t) {

    }

    public ResponseUrlDto rateLimitFallbackForModify(Throwable t) {
        return ResponseUrlDto.builder().shortUrl("Rate limit exceeded. Please try again later.").build();
    }
}
