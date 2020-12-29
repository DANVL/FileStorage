package com.filestorage.controllers;

import com.filestorage.dto.ErrorResponse;
import com.filestorage.dto.FileParamsRequest;
import com.filestorage.dto.ListFilesResponse;
import com.filestorage.dto.UploadFileResponse;
import com.filestorage.entities.File;
import com.filestorage.exceptions.InvalidSizeException;
import com.filestorage.service.FileService;
import org.elasticsearch.common.util.iterable.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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
                                    .build())
            );

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidSizeException e) {
            e.printStackTrace();

            ErrorResponse error = new ErrorResponse(false,e.getMessage()) ;
            return new ResponseEntity<>(error, HttpStatus.OK);
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
