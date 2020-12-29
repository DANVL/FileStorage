package com.filestorage.controllers;

import com.filestorage.dto.*;
import com.filestorage.entities.File;
import com.filestorage.exceptions.InvalidSizeException;
import com.filestorage.exceptions.NoSuchElementException;
import com.filestorage.exceptions.NoSuchTagsException;
import com.filestorage.service.FileService;
import org.elasticsearch.common.util.iterable.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestBody FileParamsRequest fileParamsRequest) {
        UploadFileResponse result;
        try {
            result = new UploadFileResponse(
                    fileService.saveFile(
                            File.builder()
                                    .name(fileParamsRequest.getName())
                                    .size(fileParamsRequest.getSize())
                                    .tags(new HashSet<>())
                                    .build())
            );

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidSizeException e) {
            ErrorResponse error = new ErrorResponse(false,e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id){
        try {
            fileService.deleteFile(id);
            return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ErrorResponse error = new ErrorResponse(false,"file not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<?> addTags(@PathVariable String id, @RequestBody TagsRequest tagsRequest){

        System.out.println(tagsRequest.getTags().size());
        try {
            fileService.addTags(id,tagsRequest.getTags());
            return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ErrorResponse error = new ErrorResponse(false,"file not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/tags")
    public ResponseEntity<?> removeTags(@PathVariable String id, @RequestBody TagsRequest tagsRequest) {

        try {
            fileService.removeTags(id,tagsRequest.getTags());
            return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ErrorResponse error = new ErrorResponse(false,"file not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (NoSuchTagsException e) {
            ErrorResponse error = new ErrorResponse(false,"tag not found on file");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0", required = false) int page,
                                    @RequestParam(defaultValue = "10", required = false) int size,
                                    @RequestParam(required = false) String[] tags) {
        ListFilesResponse result = new ListFilesResponse();

        if (tags != null) {
            result.setPage(fileService.getFiles(page, size, Arrays.asList(tags)));
            result.setTotal(Iterables.size(result.getPage()));
        } else {
            result.setPage(fileService.getFiles(page, size));
            result.setTotal(Iterables.size(result.getPage()));
        }


        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
