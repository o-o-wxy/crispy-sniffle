package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.RatingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingsRepository extends JpaRepository<RatingsEntity, Integer> {
    RatingsEntity findByBookIdAndAndUserId(int bookid, int userid);
}
