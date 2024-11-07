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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;

    private final RandomStringGenerator randomStringGenerator;

    @Value("${server.port}")
    private int port;

    @Value("${random}")
    private int length;

    @Override
    public ResponseUrlDto generateShortUrl(GenerateUrlRequestDto generateUrlRequestDto) throws UnknownHostException {

        String uniqueString = randomStringGenerator.generateRandomString(length);
        ShortUrlData shortUrlDataDocument = ShortUrlData.builder()
                .longUrl(generateUrlRequestDto.getLongUrl())
                .shortUrl(uniqueString)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusMonths(1))
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
    public ResponseUrlDto modifyShortUrl(ModifyUrlRequestDto modifyUrlRequestDto) throws UnknownHostException {
        ShortUrlData shortUrlData = shortUrlRepository.findByShortUrlAndIsDeletedFalse(modifyUrlRequestDto.getShortUrl());
        if(shortUrlData == null){
            throw new ShortUrlBuilderException("Invalid Short Url.");
        }
        shortUrlData.setLongUrl(modifyUrlRequestDto.getNewLongUrl());
        shortUrlData.setLastModifiedDate(LocalDateTime.now());
        shortUrlRepository.save(shortUrlData);

        return ResponseUrlDto.builder().shortUrl(generateRandomUrl(modifyUrlRequestDto.getShortUrl())).build();
    }

    @Override
    public void removeShortUrl(RemoveUrlRequestDto removeUrlRequestDto) {
        ShortUrlData shortUrlData = shortUrlRepository.findByShortUrlAndIsDeletedFalse(removeUrlRequestDto.getShortUrl());
        if(shortUrlData == null){
            throw new ShortUrlBuilderException("Invalid Short Url.");
        }
        shortUrlData.setIsDeleted(true);
        shortUrlRepository.save(shortUrlData);
    }

    @Override
    public String queryByShortUrlId(String shortUrlId) {
        ShortUrlData shortUrlData = shortUrlRepository.findByShortUrlAndIsDeletedFalse(shortUrlId);
        if(shortUrlData == null){
            throw new ShortUrlBuilderException("Invalid Short Url.");
        }
        return shortUrlData.getLongUrl();
    }
}
