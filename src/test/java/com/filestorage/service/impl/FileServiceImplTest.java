package com.filestorage.service.impl;

import com.filestorage.entities.File;
import com.filestorage.exceptions.InvalidSizeException;
import com.filestorage.exceptions.NoSuchElementException;
import com.filestorage.exceptions.NoSuchTagsException;
import com.filestorage.exceptions.NullNameException;
import com.filestorage.repository.FileRepository;
import com.filestorage.service.FileService;
import org.junit.Test;

import  org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceImplTest {

    @Autowired
    FileService fileService;

    @MockBean
    FileRepository fileRepository;

    @Test
    public void saveFile() throws InvalidSizeException, NullNameException {

        File file = File.builder().size(5).name("video.mp4").tags(new HashSet<>()).build();

        String id = "newId";
        File savedFile = File.builder().id(id).size(5).name("video.mp4").tags(new HashSet<>()).build();

        Mockito.doReturn(savedFile)
                .when(fileRepository)
                .save(file);

        String createdId = fileService.saveFile(file);

        Assert.assertEquals(id, createdId);
    }

    @Test(expected = InvalidSizeException.class)
    public void saveFileWrongSize() throws InvalidSizeException, NullNameException {
        File file = File.builder().size(-5).name("aa").build();

        fileService.saveFile(file);
    }

    @Test(expected = NullNameException.class)
    public void saveFileNullName() throws InvalidSizeException, NullNameException {
        File file = File.builder().size(5).build();

        fileService.saveFile(file);
    }

    @Test
    public void deleteFile() throws NoSuchElementException {
        String id = "id";

        Mockito.doReturn(true)
                .when(fileRepository)
                .existsById(id);

        fileService.deleteFile(id);

        Mockito.verify(fileRepository, Mockito.times(1)).deleteById(id);
    }

    @Test(expected = NoSuchElementException.class)
    public void deleteFileException() throws NoSuchElementException {
        String id = "id";

        fileService.deleteFile(id);
    }


    @Test
    public void addTags() throws NoSuchElementException {
        String id = "id";

        File file = File.builder().id(id).size(5).name("video.mp4").tags(new HashSet<>()).build();
        Optional<File> optionalFile = Optional.of(file);

        String[] tags = new String[]{"tag1","tag2"};
        File fileWithTags = File.builder().id(id).size(5).name("video.mp4").tags(Set.of(tags)).build();

        Mockito.doReturn(optionalFile)
                .when(fileRepository)
                .findById(id);

        fileService.addTags(id, Arrays.asList(tags));

        Mockito.verify(fileRepository, Mockito.times(1)).save(fileWithTags);
    }

    @Test(expected = NoSuchElementException.class)
    public void addTagsException() throws NoSuchElementException {
        String id = "id";

        String[] tags = new String[]{"tag1","tag2"};

        fileService.addTags(id, Arrays.asList(tags));
    }

    @Test(expected = NoSuchElementException.class)
    public void removeTagsNoSuchElement() throws NoSuchElementException, NoSuchTagsException {
        String id = "id";
        String[] tags = new String[]{"tag1","tag2"};

        fileService.removeTags(id,Arrays.asList(tags));
    }

    @Test(expected = NoSuchTagsException.class)
    public void removeTagsNoSuchTags() throws NoSuchElementException, NoSuchTagsException {
        String id = "id";
        String[] tags = new String[]{"tag1","tag2"};
        File file = File.builder().id(id).size(5).name("video.mp4").tags(Set.of(tags)).build();
        Optional<File> optionalFile = Optional.of(file);

        String[] removingTags = new String[]{"tag"};

        Mockito.doReturn(optionalFile)
                .when(fileRepository)
                .findById(id);

        fileService.removeTags(id,Arrays.asList(removingTags));
    }

    @Test
    public void getTags(){
        String[] tags = new String[]{"tag1","tag2"};
        File file1 = File.builder().id("id").size(5).name("video.mp4").tags(Set.of(new String[]{})).build();
        File file2 = File.builder().id("id1").size(6).name("video2.mp4").tags(Set.of(new String[]{})).build();
        File file3 = File.builder().id("id2").size(6).name("video3.mp4").tags(Set.of(tags)).build();


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

        List<File> result1 = fileService.getFiles(0,10,"vid", null);
        List<File> result2 = fileService.getFiles(0,10,"vid", tags);

        Assert.assertEquals(result1.size(), 2);
        Assert.assertEquals(result2.size(), 1);


    }




}