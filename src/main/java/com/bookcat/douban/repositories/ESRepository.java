package com.bookcat.douban.repositories;

import com.bookcat.douban.formbean.Book;

import java.util.List;

public interface ESRepository {
    List<Book> findBykey(String key);
    List<Book> moreLike(int id);
}
