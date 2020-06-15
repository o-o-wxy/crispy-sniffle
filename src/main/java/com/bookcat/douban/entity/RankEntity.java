package com.bookcat.douban.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rank", schema = "doubanDB", catalog = "")
public class RankEntity {
    private int id;
    private int userId;
    private String userName;
    private Integer sum;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "sum")
    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankEntity that = (RankEntity) o;
        return id == that.id &&
                userId == that.userId &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(sum, that.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, userName, sum);
    }
}
