package com.learn.short_url_builder.repository;

import com.learn.short_url_builder.entity.ShortUrlData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrlData, Long> {

    @Query("SELECT s FROM ShortUrlData s WHERE s.shortUrl = :shortUrl AND (s.isDeleted = false AND s.expiryDate > CURRENT_DATE)")
    Optional<ShortUrlData> findByShortUrlAndValidExpiry(String shortUrl);
}

