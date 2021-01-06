package com.filestorage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filestorage.dto.FileParamsRequest;
import com.filestorage.entities.File;
import com.filestorage.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FileControllerBasicTest {

    @Autowired
    FileController fileController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FileRepository fileRepository;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void uploadFile() throws Exception {
        String filename = "ttt.aaa";
        int size = 1;

        File file = File.builder().size(size).name(filename).tags(new HashSet<>()).build();
        File savedFile = File.builder().id("id").size(size).name(filename).tags(new HashSet<>()).build();
        Mockito.doReturn(savedFile)
                .when(fileRepository)
                .save(file);

        this.mockMvc.perform(post("/file")
                .content(asJsonString(new FileParamsRequest(filename, size)))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteFile() throws Exception {
        String id = "id";

        Mockito.doReturn(true)
                .when(fileRepository)
                .existsById(id);

        Mockito.doNothing()
                .when(fileRepository)
                .deleteById(id);

        this.mockMvc.perform(delete("/file/{id}", id))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void getFiles() throws Exception {
        this.mockMvc.perform(get("/file")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}