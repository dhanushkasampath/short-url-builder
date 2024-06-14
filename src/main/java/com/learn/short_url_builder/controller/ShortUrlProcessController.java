package com.learn.short_url_builder.controller;

import com.learn.short_url_builder.dto.GenerateUrlRequestDto;
import com.learn.short_url_builder.dto.ModifyUrlRequestDto;
import com.learn.short_url_builder.dto.RemoveUrlRequestDto;
import com.learn.short_url_builder.dto.ResponseUrlDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class ShortUrlProcessController {

    /**
     * @Desc This endpoint is used to for redirection based on shortUrlId.
     * @param shortUrlId
     * @return
     */
    @GetMapping("/{shortUrlId}")
    public RedirectView test(@PathVariable String shortUrlId){
        String externalUrl = "https://en.wikipedia.org/wiki/Sri_Lanka";
        return new RedirectView(externalUrl);
    }

    /**
     * @Desc This endpoint is used to generate the short url by long url.
     * @param generateUrlRequestDto
     * @return
     */
    @PostMapping("/api/v1/generate")
    public ResponseEntity<ResponseUrlDto> generateShortUrl(@RequestBody GenerateUrlRequestDto generateUrlRequestDto){
        ResponseUrlDto responseUrlDto = new ResponseUrlDto();
        responseUrlDto.setShortUrl("https://rb.gy/090byc");
        return new ResponseEntity<>(responseUrlDto, HttpStatus.CREATED);
    }

    /**
     * @Desc This endpoint is used to modify the short url generated previously.
     * @param modifyUrlRequestDto
     * @return
     */
    @PutMapping("/api/v1/modify")
    public ResponseEntity<ResponseUrlDto> modifyShortUrl(@RequestBody ModifyUrlRequestDto modifyUrlRequestDto){
        ResponseUrlDto responseUrlDto = new ResponseUrlDto();
        responseUrlDto.setShortUrl("https://rb.gy/090byc");
        return new ResponseEntity<>(responseUrlDto, HttpStatus.CREATED);
    }

    /**
     * @Desc This endpoint is used to remove the short url generated previously.
     * @param removeUrlRequestDto
     * @return
     */
    @DeleteMapping("/api/v1/remove")
    public ResponseEntity<ResponseUrlDto> removeShortUrl(@RequestBody RemoveUrlRequestDto removeUrlRequestDto){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
