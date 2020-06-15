package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.RatingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankRepository extends JpaRepository<RatingsEntity,Integer> {
    @Query(value =
            "SELECT user_id,COUNT(book_id) FROM ratings GROUP BY user_id"
            , nativeQuery = true)
    List<RatingsEntity> countRes();
}
