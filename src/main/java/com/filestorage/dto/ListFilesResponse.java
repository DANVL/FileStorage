package com.filestorage.dto;

import com.filestorage.entities.File;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ListFilesResponse {
    private long total;
    private List<File> page;
}
