package com.bookcat.douban.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rank", schema = "doubanDB", catalog = "")
public class RankEntity {
    private int userId;
    private Integer sum;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        return userId == that.userId &&
                Objects.equals(sum, that.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, sum);
    }
}
