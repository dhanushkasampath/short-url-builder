package com.learn.short_url_builder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ShortUrlGenerateController {

    @GetMapping("/generate")
    public String generateShortUrl(){
        return "http://localhost:8080/ADe4FGvr";
    }
}
