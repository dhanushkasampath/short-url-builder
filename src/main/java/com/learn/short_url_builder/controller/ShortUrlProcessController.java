package com.learn.short_url_builder.controller;

import com.learn.short_url_builder.dto.GenerateUrlRequestDto;
import com.learn.short_url_builder.dto.ModifyUrlRequestDto;
import com.learn.short_url_builder.dto.RemoveUrlRequestDto;
import com.learn.short_url_builder.dto.ResponseUrlDto;
import com.learn.short_url_builder.service.ShortUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ShortUrlProcessController {

    private final ShortUrlService shortUrlService;

    public ShortUrlProcessController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    /**
     * @Desc This endpoint is used to for redirection based on shortUrlId.
     * @param shortUrlId
     * @return
     */
    @GetMapping("/{shortUrlId}")
    public RedirectView test(@PathVariable String shortUrlId){
        String longUrl = shortUrlService.queryByShortUrlId(shortUrlId);
        return new RedirectView(longUrl);
    }

    /**
     * @Desc This endpoint is used to generate the short url by long url.
     * @param generateUrlRequestDto
     * @return
     */
    @PostMapping("/api/v1/generate")
    public ResponseEntity<ResponseUrlDto> generateShortUrl(@RequestBody GenerateUrlRequestDto generateUrlRequestDto){
        ResponseUrlDto responseUrlDto = shortUrlService.generateShortUrl(generateUrlRequestDto);
        return new ResponseEntity<>(responseUrlDto, HttpStatus.CREATED);
    }

    /**
     * @Desc This endpoint is used to modify the short url generated previously.
     * @param modifyUrlRequestDto
     * @return
     */
    @PutMapping("/api/v1/modify")
    public ResponseEntity<ResponseUrlDto> modifyShortUrl(@RequestBody ModifyUrlRequestDto modifyUrlRequestDto){
        ResponseUrlDto responseUrlDto = shortUrlService.modifyShortUrl();
        return new ResponseEntity<>(responseUrlDto, HttpStatus.CREATED);
    }

    /**
     * @Desc This endpoint is used to remove the short url generated previously.
     * @param removeUrlRequestDto
     * @return
     */
    @DeleteMapping("/api/v1/remove")
    public ResponseEntity<ResponseUrlDto> removeShortUrl(@RequestBody RemoveUrlRequestDto removeUrlRequestDto){
        shortUrlService.removeShortUrl(removeUrlRequestDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
