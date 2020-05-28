package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UsersEntity, Integer> {
    UsersEntity findByUserName(String name);
    UsersEntity findByUserId(int id);
}
