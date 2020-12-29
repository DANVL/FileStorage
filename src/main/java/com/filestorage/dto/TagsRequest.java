package com.filestorage.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagsRequest {
    private List<String> tags;
}
