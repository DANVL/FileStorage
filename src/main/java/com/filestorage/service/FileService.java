package com.filestorage.service;

import com.filestorage.entities.File;

import java.nio.file.NoSuchFileException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public interface FileService {
    String saveFile(File file) throws InvalidPropertiesFormatException;
    void deleteFile(String id) throws NoSuchFileException;
    void addTags(String id, List<String> tags);
    void removeTags(String id, List<String> tags) throws NoSuchFieldException;
    Iterable<File> getFiles(List<String> tags, int page,int size);

}
