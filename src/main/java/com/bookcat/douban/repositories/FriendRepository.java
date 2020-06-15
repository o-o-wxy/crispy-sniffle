package com.bookcat.douban.repositories;

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

}
