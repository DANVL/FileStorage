package com.filestorage.service;

import com.filestorage.entities.File;
import com.filestorage.exceptions.InvalidSizeException;
import com.filestorage.exceptions.NoSuchElementException;
import com.filestorage.exceptions.NoSuchTagsException;
import com.filestorage.exceptions.NullNameException;

import java.util.List;

public interface FileService {
    String saveFile(File file) throws InvalidSizeException, NullNameException;

    void deleteFile(String id) throws NoSuchElementException;

    void addTags(String id, List<String> tags) throws NoSuchElementException;

    void removeTags(String id, List<String> tags) throws NoSuchElementException, NoSuchTagsException;

    List<File> getFiles(int page, int size, String name, String[] tags);

}
