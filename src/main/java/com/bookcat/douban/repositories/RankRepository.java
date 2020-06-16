package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RankRepository extends JpaRepository<RankEntity,Integer> {
    @Query(value =
            "SELECT user_id,COUNT(book_id) sum FROM ratings GROUP BY user_id ORDER BY COUNT(book_id) DESC"
            , nativeQuery = true)
    List<RankEntity> countRes();
}
