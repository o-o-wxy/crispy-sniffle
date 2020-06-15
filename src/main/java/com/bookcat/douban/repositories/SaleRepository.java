package com.bookcat.douban.repositories;

import com.bookcat.douban.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<SaleEntity,Integer> {
    List<SaleEntity> findByBookid(int bookid);

}
