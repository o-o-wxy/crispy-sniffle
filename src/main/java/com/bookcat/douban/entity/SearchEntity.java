package com.bookcat.douban.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "search", schema = "doubanDB", catalog = "")
public class SearchEntity {
    private int searchId;
    private int userId;
    private String content;

    @Id
    @Column(name = "search_id")
    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    @Basic
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchEntity that = (SearchEntity) o;
        return searchId == that.searchId &&
                userId == that.userId &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchId, userId, content);
    }
}
