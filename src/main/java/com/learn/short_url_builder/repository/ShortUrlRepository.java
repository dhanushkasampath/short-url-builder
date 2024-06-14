package com.learn.short_url_builder.repository;

import com.learn.short_url_builder.model.ShortUrlDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends MongoRepository<ShortUrlDocument, Long> {
}
