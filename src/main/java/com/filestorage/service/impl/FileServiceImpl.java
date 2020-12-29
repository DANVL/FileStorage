package com.filestorage.service.impl;

import com.filestorage.entities.File;
import com.filestorage.exceptions.InvalidSizeException;
import com.filestorage.exceptions.NoSuchElementException;
import com.filestorage.exceptions.NoSuchTagsException;
import com.filestorage.repository.FileRepository;
import com.filestorage.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.util.iterable.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public String saveFile(File file) throws InvalidSizeException {
        int size = file.getSize();
        if (size <= 0) {
            log.error("File with wrong size found");
            throw new InvalidSizeException("Wrong size of " + size + " found");
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
            edited.addTags(tags);

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

            if (edited.removeTags(tags)) {
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
    public Iterable<File> getFiles(int page, int size, List<String> tags) {
        Iterable<File> result = fileRepository.findAll(PageRequest.of(page, size))
                .filter(f -> f.getTags().containsAll(tags));

        log.info("found " + Iterables.size(result) + " files");

        return result;
    }

    @Override
    public Iterable<File> getFiles(int page, int size) {
        Iterable<File> result = fileRepository.findAll(PageRequest.of(page, size));

        log.info("found " + Iterables.size(result) + " files");

        return result;
    }


}
