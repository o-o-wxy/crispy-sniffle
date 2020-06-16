package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.BooksEntity;
import com.bookcat.douban.entity.FriendsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface FriendRepository extends JpaRepository<FriendsEntity,Integer> {
    List<FriendsEntity> findAllByUserId(int id);
    FriendsEntity findByFriendIdAndUserId(int fid,int uid);

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO friends (user_id,friend_id,state,time) VALUES (?1, ?2, 1,NOW())"
            , nativeQuery = true)
    int addFriend(int uid,int fid);

    @Transactional
    @Modifying
    @Query(value =
            "INSERT INTO friends (user_id,friend_id,state,time) VALUES (?1, ?2, 0,NOW())"
            , nativeQuery = true)
    int addFriend2(int fid,int uid);

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE friends SET state = 2,time = NOW() WHERE user_id = ?1 AND friend_id = ?2"
            , nativeQuery = true)
    int agree(int uid,int fid);

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE friends SET state = -1,time = NOW() WHERE user_id = ?1 AND friend_id = ?2"
            , nativeQuery = true)
    int disagree(int uid,int fid);

    @Transactional
    @Modifying
    @Query(value =
            "UPDATE friends SET state = -2,time = NOW() WHERE user_id = ?1 AND friend_id = ?2"
            , nativeQuery = true)
    int delete(int uid,int fid);

    @Query(value =
            "SELECT * FROM friends WHERE user_id = ?1 AND friend_id = ?2 AND state = 2"
            , nativeQuery = true)
    FriendsEntity ifFriend(int uid,int fid);

}
