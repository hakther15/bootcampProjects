package com.lendingcatalog.model;

import com.lendingcatalog.util.FileStorageService;
import com.lendingcatalog.util.exception.FileStorageException;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Tool implements CatalogItem {
        String id;
        String type;
        String manufacturer;
        int count;


        public Tool(String type, String manufacturer, int count){
            this.type = type;
            this.manufacturer = manufacturer;
            this.count = count;
        }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String toString(){
            return "Tool type: " + type + System.lineSeparator()
                    + "- Manufacturer: " + manufacturer + System.lineSeparator()
                    + "- Tool count: " + count + System.lineSeparator()
                    + "- Id: " + id + System.lineSeparator();
    }
    @Override
    public boolean matchesName(String searchStr) {
        return type.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesCreator(String searchStr) {
        return manufacturer.toLowerCase().contains(searchStr.toLowerCase());
    }

    @Override
    public boolean matchesYear(int searchYear) {
        return false;
    }

    @Override
    public void registerItem() throws FileNotFoundException, FileStorageException {
        id = UUID.randomUUID().toString();
        FileStorageService.writeContentsToFile(LocalDate.now() + " " + LocalTime.now() + toString(), "src/main/resources/logs/Tool.log", true);
    }
}
