package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.BooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface BookRepository extends JpaRepository<BooksEntity, Integer> {
    @Query(value =
            "SELECT * FROM books WHERE MATCH (name,sub_name,series,publisher,authors,tags) AGAINST (?1 IN NATURAL LANGUAGE MODE)"
            , nativeQuery = true)
    List<BooksEntity> findByKey(String key);

//    List<BooksEntity> findByNameOrsOrSubNameOrsOrSeriesOrpOrPublisherOrAuthorsOrTagsLike(String content);

    BooksEntity findById(int id);

    @Query(value =
            "select * from books order by douban_score desc limit 8; "
            , nativeQuery = true)
    List<BooksEntity> findRecommend();

    @Query(value =
            "select * from books order by douban_votes desc limit 8;"
            , nativeQuery = true)
    List<BooksEntity> findHot();

    @Query(value =
            "select * from books order by tags LIKE '%新书%' desc limit 8"
            , nativeQuery = true)
    List<BooksEntity> findLatest();

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE books SET count = count+1 WHERE id = ?1 "
            , nativeQuery = true)
    int look( int id );
}
