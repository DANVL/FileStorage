package com.filestorage.controllers;

import com.filestorage.dto.FileParams;
import com.filestorage.entities.File;
import com.filestorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestBody FileParams fileParams){
        String result = fileService.saveFile(
                File.builder()
                        .name(fileParams.getName())
                        .size(fileParams.getSize())
                        .build());
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(fileService.getFiles(new ArrayList<>(),0,0), HttpStatus.OK);
    }
}
