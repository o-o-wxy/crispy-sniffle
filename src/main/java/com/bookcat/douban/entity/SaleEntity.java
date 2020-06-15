package com.bookcat.douban.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sale", schema = "doubanDB", catalog = "")
public class SaleEntity {
    private int id;
    private String href;
    private Integer userid;
    private Integer bookid;
    private String username;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "href")
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Basic
    @Column(name = "userid")
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "bookid")
    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleEntity that = (SaleEntity) o;
        return id == that.id &&
                Objects.equals(href, that.href) &&
                Objects.equals(userid, that.userid) &&
                Objects.equals(bookid, that.bookid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, href, userid, bookid);
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
