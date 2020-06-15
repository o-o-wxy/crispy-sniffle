package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<UsersEntity, Integer> {
    UsersEntity findByUserName(String name);
    UsersEntity findByUserId(int id);
    boolean existsById(int id);
    @Transactional
    @Modifying
    @Query(value =
            "UPDATE books SET douban_score = (douban_score*douban_votes+?1)/(douban_votes+1), douban_votes = douban_votes+1 WHERE id = ?2"
            , nativeQuery = true)
    int score(int score,int id);

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO ratings (user_id,book_id,score) VALUES (?1, ?2, ?3)"
            , nativeQuery = true)
    int rating(int userID,int bookID,int rate);


}
