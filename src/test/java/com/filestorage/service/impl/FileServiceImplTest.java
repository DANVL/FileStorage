package com.filestorage.service.impl;

import com.filestorage.entities.File;
import com.filestorage.exceptions.*;
import com.filestorage.exceptions.NoSuchElementException;
import com.filestorage.repository.FileRepository;
import com.filestorage.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FileServiceImplTest {

    @Autowired
    FileService fileService;

    @MockBean
    FileRepository fileRepository;

    @Test
    void saveFile() throws InvalidSizeException, IllegalSymbolsException, NullNameException {
        File file = File.builder().size(5).name("video.aaa").tags(new HashSet<>()).build();

        String id = "newId";
        File savedFile = File.builder().id(id).size(5).name("video.aaa").tags(new HashSet<>()).build();

        Mockito.doReturn(savedFile)
                .when(fileRepository)
                .save(file);

        String createdId = fileService.saveFile(file);

        Assertions.assertEquals(id, createdId);
    }

    @Test
    public void saveFileWrongSize() {
        File file = File.builder().size(-5).name("aa").build();

        Assertions.assertThrows(InvalidSizeException.class, () -> fileService.saveFile(file));

    }

    @Test
    public void saveFileNullName() {
        File file = File.builder().size(5).build();

        Assertions.assertThrows(NullNameException.class, () -> fileService.saveFile(file));

    }

    @Test
    public void deleteFile() throws NoSuchElementException {
        String id = "id";

        Mockito.doReturn(true)
                .when(fileRepository)
                .existsById(id);

        Mockito.doNothing()
                .when(fileRepository)
                .deleteById(id);

        fileService.deleteFile(id);

        Mockito.verify(fileRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void deleteFileException() {
        String id = "id";

        Assertions.assertThrows(NoSuchElementException.class, () -> fileService.deleteFile(id));
    }

    @Test
    public void addTags() throws NoSuchElementException {
        String id = "id";

        File file = File.builder().id(id).size(5).name("video.aaa").tags(new HashSet<>()).build();
        Optional<File> optionalFile = Optional.of(file);

        String[] tags = new String[]{"tag1", "tag2"};
        File fileWithTags = File.builder().id(id).size(5).name("video.aaa").tags(Set.of(tags)).build();

        Mockito.doReturn(optionalFile)
                .when(fileRepository)
                .findById(id);

        fileService.addTags(id, Arrays.asList(tags));

        Mockito.verify(fileRepository, Mockito.times(1)).save(fileWithTags);
    }

    @Test
    public void addTagsException() {
        String id = "id";

        String[] tags = new String[]{"tag1", "tag2"};

        Assertions.assertThrows(NoSuchElementException.class, () -> fileService.addTags(id, Arrays.asList(tags)));
    }

    @Test
    public void removeTagsNoSuchElement() {
        String id = "id";
        String[] tags = new String[]{"tag1", "tag2"};

        Assertions.assertThrows(NoSuchElementException.class, () -> fileService.removeTags(id, Arrays.asList(tags)));
    }

    @Test
    public void removeTagsNoSuchTags() {
        String id = "id";
        String[] tags = new String[]{"tag1", "tag2"};
        File file = File.builder().id(id).size(5).name("video.aaa").tags(Set.of(tags)).build();
        Optional<File> optionalFile = Optional.of(file);

        String[] removingTags = new String[]{"tag"};

        Mockito.doReturn(optionalFile)
                .when(fileRepository)
                .findById(id);

        Assertions.assertThrows(NoSuchTagsException.class,
                () -> fileService.removeTags(id, Arrays.asList(removingTags)));
    }

    @Test
    public void getTags(){
        String[] tags = new String[]{"tag1","tag2"};
        File file1 = File.builder().id("id").size(5).name("video.aaa").tags(Set.of(new String[]{})).build();
        File file2 = File.builder().id("id1").size(6).name("video2.aaa").tags(Set.of(new String[]{})).build();
        File file3 = File.builder().id("id2").size(6).name("video3.aaa").tags(Set.of(tags)).build();


        List<File> files1 = new ArrayList<>(){
            {
                add(file1);
                add(file2);
            }
        };

        List<File> files2 = new ArrayList<>(){
            {
                add(file3);
            }
        };


        Mockito.doReturn(files1)
                .when(fileRepository)
                .findAllByNameLike("vid", PageRequest.of(0,10));

        Mockito.doReturn(files2)
                .when(fileRepository)
                .findAllByNameLikeAndTags("vid", Set.of(tags), PageRequest.of(0,10));

        List<File> result1 = fileService.getFiles(0,10,"vid");
        List<File> result2 = fileService.getFiles(0,10,"vid", tags);

        Assertions.assertEquals(result1.size(), 2);
        Assertions.assertEquals(result2.size(), 1);


    }
}