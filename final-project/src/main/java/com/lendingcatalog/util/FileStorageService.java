package com.lendingcatalog.util;

import com.lendingcatalog.util.exception.FileStorageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileStorageService {

    // Requirement: File I/O
    public static void writeContentsToFile(String contents, String filename, boolean appendFile) throws FileStorageException, FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, appendFile))) {
            writer.print(contents);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readContentsOfFile(String filename) throws FileStorageException, FileNotFoundException {
        try (Scanner reader = new Scanner(new File(filename))) {
            List<String> itemNames = new ArrayList<>();
            while (reader.hasNext()) {
                String line = reader.nextLine();
                itemNames.add(line);
            }
            return itemNames;
        } catch (FileNotFoundException e) {
            throw new FileStorageException(e.getMessage());
        }
    }
}
