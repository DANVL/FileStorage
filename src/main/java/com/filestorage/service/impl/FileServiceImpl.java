package com.filestorage.service.impl;

import com.filestorage.entities.File;
import com.filestorage.exceptions.*;
import com.filestorage.repository.FileRepository;
import com.filestorage.service.FileService;
import com.filestorage.utils.FileNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public String saveFile(File file) throws InvalidSizeException, NullNameException, IllegalSymbolsException {
        int size = file.getSize();

        if (file.getName() == null) {
            log.error("File with null name found");
            throw new NullNameException("Name cannot be null");
        }

        if (FileNameUtils.isFileNameContainsInvalidCharacters(file.getName())) {
            log.error("File with illegal characters in name found");
            throw new IllegalSymbolsException("Name contains illegal characters");
        }

        if (size < 0) {
            log.error("File with wrong size found");
            throw new InvalidSizeException("Wrong size of " + size + " found");
        }

        String tag = FileNameUtils.tagByFileName(file.getName());

        if (tag != null) {
            file.addTag(tag);
        }

        file = fileRepository.save(file);

        log.info("New file saved");

        return file.getId();
    }

    @Override
    public void deleteFile(String id) throws NoSuchElementException {
        if (fileRepository.existsById(id)) {
            fileRepository.deleteById(id);

            log.info("File deleted");

        } else {
            log.error("Element not found");
            throw new NoSuchElementException("No element with id: " + id + " found");
        }

    }

    @Override
    public void addTags(String id, List<String> tags) throws NoSuchElementException {
        Optional<File> file = fileRepository.findById(id);

        if (file.isPresent()) {
            File edited = file.get();
            Set<String> newTags = edited.getTags();

            if (newTags == null)
                newTags = new HashSet<>();

            newTags.addAll(tags);
            edited.setTags(newTags);

            fileRepository.save(edited);

            log.info("Tags added");

        } else {
            log.error("Element not found");
            throw new NoSuchElementException("No element with id: " + id + " found");
        }
    }

    @Override
    public void removeTags(String id, List<String> tags) throws NoSuchTagsException, NoSuchElementException {
        Optional<File> file = fileRepository.findById(id);

        if (file.isPresent()) {
            File edited = file.get();
            Set<String> newTags = edited.getTags();

            if (newTags.containsAll(tags)) {
                newTags.removeAll(tags);
                edited.setTags(newTags);

                fileRepository.save(edited);

                log.info("Tags removed");
            } else {
                log.error("Some tags are not included in file");
                throw new NoSuchTagsException("Some tags are not found for element with id: " + id);
            }

        } else {
            log.error("Element not found");
            throw new NoSuchElementException("No element with id: " + id + " found");
        }
    }

    @Override
    public List<File> getFiles(int page, int size, String name, String[] tags) {

        List<File> result;

        result = fileRepository
                .findAllByNameLikeAndTags(name, Set.of(tags), PageRequest.of(page, size));

        log.info("Found " + result.size() + " files");

        return result;
    }

    @Override
    public List<File> getFiles(int page, int size, String name) {

        List<File> result;

        result = fileRepository
                .findAllByNameLike(name, PageRequest.of(page, size));


        log.info("Found " + result.size() + " files");

        return result;
    }

    @Override
    public long getTotal(String name, String[] tags) {
        long result = fileRepository.countAllByNameLikeAndTags(name, Set.of(tags));
        log.info("Total of " + result + " found");
        return result;
    }

    @Override
    public long getTotal(String name) {
        long result = fileRepository.countAllByNameLike(name);
        log.info("Total of " + result + " found");
        return result;
    }
}
