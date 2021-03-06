package com.filestorage.controllers;

import com.filestorage.dto.*;
import com.filestorage.entities.File;
import com.filestorage.exceptions.*;
import com.filestorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

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
        try {
            UploadFileResponse result = new UploadFileResponse(
                    fileService.saveFile(
                            File.builder()
                                    .name(fileParamsRequest.getName())
                                    .size(fileParamsRequest.getSize())
                                    .tags(new HashSet<>())
                                    .build())
            );

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidSizeException | NullNameException | IllegalSymbolsException e) {
            ErrorResponse error = new ErrorResponse(false, e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        try {
            fileService.deleteFile(id);
            return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ErrorResponse error = new ErrorResponse(false, "file not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<?> addTags(@PathVariable String id, @RequestBody List<String> tagsRequest) {

        System.out.println(tagsRequest.size());
        try {
            fileService.addTags(id, tagsRequest);
            return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ErrorResponse error = new ErrorResponse(false, "file not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/tags")
    public ResponseEntity<?> removeTags(@PathVariable String id, @RequestBody List<String> tagsRequest) {

        try {
            fileService.removeTags(id, tagsRequest);
            return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ErrorResponse error = new ErrorResponse(false, "file not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (NoSuchTagsException e) {
            ErrorResponse error = new ErrorResponse(false, "tag not found on file");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(defaultValue = "0", required = false) int page,
                                    @RequestParam(defaultValue = "10", required = false) int size,
                                    @RequestParam(required = false) String[] tags,
                                    @RequestParam(defaultValue = "", required = false) String q) {
        ListFilesResponse result = new ListFilesResponse();

        if (tags != null) {
            result.setPage(fileService.getFiles(page, size, q, tags));
            result.setTotal(fileService.getTotal(q, tags));
        } else {
            result.setPage(fileService.getFiles(page, size, q));
            result.setTotal(fileService.getTotal(q));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
