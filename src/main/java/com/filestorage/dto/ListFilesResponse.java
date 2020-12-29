package com.filestorage.dto;

import com.filestorage.entities.File;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ListFilesResponse {
    private long total;
    private Iterable<File> page;
}
