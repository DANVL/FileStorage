package com.filestorage.service;

import com.filestorage.entities.File;
import com.filestorage.exceptions.NoSuchElementException;
import com.filestorage.exceptions.InvalidSizeException;
import com.filestorage.exceptions.NoSuchTagsException;

import java.nio.file.NoSuchFileException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public interface FileService {
    String saveFile(File file) throws InvalidSizeException;
    void deleteFile(String id) throws NoSuchElementException;
    void addTags(String id, List<String> tags) throws NoSuchElementException;
    void removeTags(String id, List<String> tags) throws NoSuchElementException, NoSuchTagsException;
    Iterable<File> getFiles(int page,int size, List<String> tags);
    Iterable<File> getFiles(int page,int size);

}
