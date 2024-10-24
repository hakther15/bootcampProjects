package com.lendingcatalog.model;

import com.lendingcatalog.util.exception.FileStorageException;

import java.io.FileNotFoundException;
import java.time.LocalDate;

public interface CatalogItem {
    boolean matchesName(String searchStr);
    boolean matchesCreator(String searchStr);
    boolean matchesYear(int searchYear);
    void registerItem() throws FileNotFoundException, FileStorageException;


}


