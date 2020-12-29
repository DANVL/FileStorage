package com.filestorage.service.impl;

import com.filestorage.entities.File;
import com.filestorage.repository.FileRepository;
import com.filestorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository){
        this.fileRepository = fileRepository;

    }

    @Override
    public String saveFile(File file) throws InvalidPropertiesFormatException {
        if(file.getSize() <= 0){
            throw new InvalidPropertiesFormatException("");
        }
        file = fileRepository.save(file);
        return file.getId();
    }

    @Override
    public void deleteFile(String id) throws NoSuchFileException {
        if(fileRepository.existsById(id)){
            fileRepository.deleteById(id);
        }else{
            throw new NoSuchFileException("");
        }

    }

    @Override
    public void addTags(String id, List<String> tags) {
        File file = fileRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        file.addTags(tags);

        fileRepository.save(file);
    }

    @Override
    public void removeTags(String id, List<String> tags) throws NoSuchFieldException {
        File file = fileRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        if(file.removeTags(tags)){
            fileRepository.save(file);
        }else{
            throw new NoSuchFieldException();
        }


    }

    @Override
    public Iterable<File> getFiles(List<String> tags, int page, int size) {
        if(tags.size() > 0){
            return fileRepository.findAll(PageRequest.of(page, size));
        }else{
            return fileRepository.findAll(PageRequest.of(page, size))
                    .filter(f -> f.getTags().containsAll(tags));
        }

    }


}
