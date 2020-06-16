package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Integer> {
    List<CommentEntity> findAllByBookId(int id);

    List<CommentEntity> findAllByUserId(int id);

    CommentEntity findByUserId(int id);

    List<CommentEntity> findAll();

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO comment (book_id, title, context, user_id, user_name, create_time) VALUES (?1, ?2, ?3, ?4, ?5, NOW())"
            , nativeQuery = true)
    int comment(int bookID, String title, String context, int userID, String userName);

    @Transactional
    @Modifying
    @Query(value =
            "DELETE FROM comment WHERE user_id = ?1 AND id = ?2"
            , nativeQuery = true)
    int delComment(int uid, int id);
}
