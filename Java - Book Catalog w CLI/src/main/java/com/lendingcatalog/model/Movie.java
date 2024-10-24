package com.lendingcatalog.model;

import com.lendingcatalog.util.FileStorageService;
import com.lendingcatalog.util.exception.FileStorageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;

public class Movie implements CatalogItem {
    String id;
    String name;
    String director;
    LocalDate releaseDate;

    public Movie(String name, String director, String releaseDateInput) {
        this.name = name;
        this.director = director;
        releaseDate = LocalDate.parse(releaseDateInput);
    }

    public String getId() {
        return id;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getDirector() {
        return director;
    }

    public String getName() {
        return name;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String toString() {
        return "'" + name + "'" + System.lineSeparator()
                + "- Directed by " + director + System.lineSeparator()
                + "- Released on " + releaseDate + System.lineSeparator()
                + "- Id: " + id + System.lineSeparator();
    }

    @Override
    public boolean matchesName(String searchStr) {
        return name.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesCreator(String searchStr) {
        return director.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesYear(int searchYear) {
        return releaseDate.equals(searchYear);
    }

    @Override
    public void registerItem() throws FileNotFoundException, FileStorageException {
        id = UUID.randomUUID().toString();
        FileStorageService.writeContentsToFile(LocalDate.now() + " " + LocalTime.now() + toString(), "src/main/resources/logs/Movie.log", true);
    }
}
