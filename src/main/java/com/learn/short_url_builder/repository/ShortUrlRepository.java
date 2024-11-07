package com.learn.short_url_builder.repository;

import com.learn.short_url_builder.entity.ShortUrlData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrlData, Long> {
}
