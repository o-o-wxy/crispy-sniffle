package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Integer> {
    List<ActivityEntity> findBySelected(int selected);
    ActivityEntity findById(int id);
}
