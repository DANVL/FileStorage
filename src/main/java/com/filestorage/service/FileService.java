package com.filestorage.service;

import com.filestorage.entities.File;
import com.filestorage.exceptions.*;

import java.util.List;

public interface FileService {
    String saveFile(File file) throws InvalidSizeException, NullNameException, IllegalSymbolsException;

    void deleteFile(String id) throws NoSuchElementException;

    void addTags(String id, List<String> tags) throws NoSuchElementException;

    void removeTags(String id, List<String> tags) throws NoSuchElementException, NoSuchTagsException;

    List<File> getFiles(int page, int size, String name, String[] tags);
    List<File> getFiles(int page, int size, String name);

    long getTotal(String name, String[] tags);
    long getTotal(String name);

}
