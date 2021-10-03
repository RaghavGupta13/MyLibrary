package com.corsair.mylibrary;

public class LibraryModel {

    private int id;
    private String book_title;
    private String book_author;
    private String book_publisher;
    private int book_pages;
    private int book_published_year;
    private String book_category;
    private String book_summary;
    private byte[] book_image;

    public LibraryModel(int id, String book_title, String book_author, String book_publisher, int book_pages, int book_published_year, String book_category, String book_summary, byte[] book_image) {
        this.id = id;
        this.book_title = book_title;
        this.book_author = book_author;
        this.book_publisher = book_publisher;
        this.book_summary = book_summary;
        this.book_category = book_category;
        this.book_pages = book_pages;
        this.book_published_year = book_published_year;
        this.book_image = book_image;
    }

    public LibraryModel() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getBook_publisher() {
        return book_publisher;
    }

    public void setBook_publisher(String book_publisher) {
        this.book_publisher = book_publisher;
    }

    public String getBook_summary() {
        return book_summary;
    }

    public void setBook_summary(String book_summary) {
        this.book_summary = book_summary;
    }

    public String getBook_category() {
        return book_category;
    }

    public void setBook_category(String book_category) {
        this.book_category = book_category;
    }

    public int getBook_pages() {
        return book_pages;
    }

    public void setBook_pages(int book_pages) {
        this.book_pages = book_pages;
    }

    public int getBook_published_year() {
        return book_published_year;
    }

    public void setBook_published_year(int book_published_year) {
        this.book_published_year = book_published_year;
    }

    public byte[] getBook_image() {
        return book_image;
    }
//
//    public void setBook_image_resource_id(int book_image_resource_id) {
//        this.book_image_resource_id = book_image_resource_id;
//    }

    @Override
    public String toString() {
        return "LibraryModel{" +
                "id=" + id +
                ", book_title='" + book_title + '\'' +
                ", book_author='" + book_author + '\'' +
                ", book_publisher='" + book_publisher + '\'' +
                ", book_summary='" + book_summary + '\'' +
                ", book_category='" + book_category + '\'' +
                ", book_pages=" + book_pages +
                ", book_published_year=" + book_published_year +
                ", book_image_resource_id=" + "" +
                '}';
    }
}
