package com.lendingcatalog.model;

import com.lendingcatalog.util.FileStorageService;
import com.lendingcatalog.util.exception.FileStorageException;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;

public class Book implements CatalogItem{

    String id;
    String title;
    String author;
    LocalDate publishDate;

    public Book(String title, String author, String publishDateInput){
        this.title = title;
        this.author = author;
        publishDate = LocalDate.parse(publishDateInput);
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString(){
        return "'" +  title + "'" + System.lineSeparator()
                + "- Written by: " + author + System.lineSeparator()
                + "- Published on: " + publishDate + System.lineSeparator()
                + "- Id: " + id + System.lineSeparator();
    }

    @Override
    public boolean matchesName(String searchStr) {
        return title.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesCreator(String searchStr) {
        return author.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesYear(int searchYear) {
        return publishDate.equals(searchYear);
    }

    @Override
    public void registerItem() throws FileNotFoundException, FileStorageException {
        id = UUID.randomUUID().toString();
        FileStorageService.writeContentsToFile(LocalDate.now() + " " + LocalTime.now() + toString(), "src/main/resources/logs/Book.log", true);
    }
}
