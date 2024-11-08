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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
    public static final String RATE_LIMIT_EXCEEDED_PLEASE_TRY_AGAIN_LATER = "Rate limit exceeded. Please try again later.";
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
    @CachePut(cacheNames = "query-short-url", key = "#modifyUrlRequestDto.shortUrl")
    @RateLimiter(name = "myServiceRateLimiter", fallbackMethod = "rateLimitFallbackForModify")
    public String modifyShortUrl(ModifyUrlRequestDto modifyUrlRequestDto) throws UnknownHostException {
        Optional<ShortUrlData> shortUrlDataOptional = shortUrlRepository.findByShortUrlAndValidExpiry(modifyUrlRequestDto.getShortUrl());
        if(shortUrlDataOptional.isEmpty()){
            throw new ShortUrlBuilderException(INVALID_SHORT_URL);
        }
        ShortUrlData shortUrlData = shortUrlDataOptional.get();
        shortUrlData.setLongUrl(modifyUrlRequestDto.getNewLongUrl());
        shortUrlData.setLastModifiedDate(LocalDateTime.now());
        shortUrlData.setExpiryDate(LocalDateTime.now().plusDays(noOfDaysToExpire));
        shortUrlRepository.save(shortUrlData);

        return modifyUrlRequestDto.getNewLongUrl(); //cache is working with the help of return type and return value
        // in both cache generating and updating. So make sure the value we return when generating cache and updating
        // cache is same and in updating cache, its the next value we updated
    }

    @Override
    @CacheEvict(cacheNames = "query-short-url", key = "#removeUrlRequestDto.shortUrl") // it's not mandatory to that the return type of this method should not match the @Cacheable or @CachePut
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
    @Cacheable(cacheNames = "query-short-url", key = "#shortUrl") // '#shortUrl '<- this is the key for cache | 'query-short-url' <- This is the cache object
    @RateLimiter(name = "myServiceRateLimiter", fallbackMethod = "rateLimitFallbackForQuery")
    public String queryByShortUrlId(String shortUrl) {
        log.info("Service layer received the request | to queryByShortUrlId");
        Optional<ShortUrlData> shortUrlData = shortUrlRepository.findByShortUrlAndValidExpiry(shortUrl);
        if(shortUrlData.isEmpty()){
            throw new ShortUrlBuilderException(INVALID_SHORT_URL);
        }
        return shortUrlData.get().getLongUrl(); //<-this is the value for cache | in update method also we need to return the new long url
    }

    public String rateLimitFallbackForQuery(Throwable t) {
        return RATE_LIMIT_EXCEEDED_PLEASE_TRY_AGAIN_LATER;
    }

    public ResponseUrlDto rateLimitFallbackForGenerate(Throwable t) {
        return ResponseUrlDto.builder().shortUrl(RATE_LIMIT_EXCEEDED_PLEASE_TRY_AGAIN_LATER).build();
    }

    public void rateLimitFallbackForRemoval(Throwable t) {
        // This is a void method
    }

    public String rateLimitFallbackForModify(Throwable t) {
        return RATE_LIMIT_EXCEEDED_PLEASE_TRY_AGAIN_LATER;
    }
}
