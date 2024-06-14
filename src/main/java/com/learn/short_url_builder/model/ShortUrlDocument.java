package com.learn.short_url_builder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "short_url_document")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrlDocument {

    @Id
    private Long id;
    private String shortUrl;
    private String longUrl;
    private LocalDateTime expiryDate;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
