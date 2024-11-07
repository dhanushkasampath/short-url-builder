package com.learn.short_url_builder.service.impl;

import com.learn.short_url_builder.dto.GenerateUrlRequestDto;
import com.learn.short_url_builder.dto.RemoveUrlRequestDto;
import com.learn.short_url_builder.dto.ResponseUrlDto;
import com.learn.short_url_builder.entity.ShortUrlData;
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
    public ResponseUrlDto modifyShortUrl() {
        return ResponseUrlDto.builder().shortUrl("https://rb.gy/090byc").build();
    }

    @Override
    public void removeShortUrl(RemoveUrlRequestDto removeUrlRequestDto) {

    }

    @Override
    public String queryByShortUrlId(String shortUrlId) {
        return "https://en.wikipedia.org/wiki/Sri_Lanka";
    }
}
