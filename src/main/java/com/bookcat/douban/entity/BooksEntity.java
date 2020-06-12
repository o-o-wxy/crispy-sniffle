package com.bookcat.douban.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "books", schema = "doubanDB", catalog = "")
public class BooksEntity {
    private int id;
    private String slug;
    private String name;
    private String subName;
    private String altName;
    private String cover;
    private String summary;
    private String authors;
    private String authorIntro;
    private String translators;
    private String series;
    private String publisher;
    private String publishDate;
    private String pages;
    private String price;
    private String binding;
    private String isbn;
    private String tags;
    private int doubanId;
    private double doubanScore;
    private int doubanVotes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer count;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "slug")
    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "sub_name")
    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    @Basic
    @Column(name = "alt_name")
    public String getAltName() {
        return altName;
    }

    public void setAltName(String altName) {
        this.altName = altName;
    }

    @Basic
    @Column(name = "cover")
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Basic
    @Column(name = "summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "authors")
    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    @Basic
    @Column(name = "author_intro")
    public String getAuthorIntro() {
        return authorIntro;
    }

    public void setAuthorIntro(String authorIntro) {
        this.authorIntro = authorIntro;
    }

    @Basic
    @Column(name = "translators")
    public String getTranslators() {
        return translators;
    }

    public void setTranslators(String translators) {
        this.translators = translators;
    }

    @Basic
    @Column(name = "series")
    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Basic
    @Column(name = "publisher")
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Basic
    @Column(name = "publish_date")
    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    @Basic
    @Column(name = "pages")
    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    @Basic
    @Column(name = "price")
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Basic
    @Column(name = "binding")
    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    @Basic
    @Column(name = "isbn")
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Basic
    @Column(name = "tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Basic
    @Column(name = "douban_id")
    public int getDoubanId() {
        return doubanId;
    }

    public void setDoubanId(int doubanId) {
        this.doubanId = doubanId;
    }

    @Basic
    @Column(name = "douban_score")
    public double getDoubanScore() {
        return doubanScore;
    }

    public void setDoubanScore(double doubanScore) {
        this.doubanScore = doubanScore;
    }

    @Basic
    @Column(name = "douban_votes")
    public int getDoubanVotes() {
        return doubanVotes;
    }

    public void setDoubanVotes(int doubanVotes) {
        this.doubanVotes = doubanVotes;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "updated_at")
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Basic
    @Column(name = "count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooksEntity that = (BooksEntity) o;
        return id == that.id &&
                doubanId == that.doubanId &&
                doubanVotes == that.doubanVotes &&
                Objects.equals(slug, that.slug) &&
                Objects.equals(name, that.name) &&
                Objects.equals(subName, that.subName) &&
                Objects.equals(altName, that.altName) &&
                Objects.equals(cover, that.cover) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(authors, that.authors) &&
                Objects.equals(authorIntro, that.authorIntro) &&
                Objects.equals(translators, that.translators) &&
                Objects.equals(series, that.series) &&
                Objects.equals(publisher, that.publisher) &&
                Objects.equals(publishDate, that.publishDate) &&
                Objects.equals(pages, that.pages) &&
                Objects.equals(price, that.price) &&
                Objects.equals(binding, that.binding) &&
                Objects.equals(isbn, that.isbn) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(doubanScore, that.doubanScore) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug, name, subName, altName, cover, summary, authors, authorIntro, translators, series, publisher, publishDate, pages, price, binding, isbn, tags, doubanId, doubanScore, doubanVotes, createdAt, updatedAt, count);
    }
}
