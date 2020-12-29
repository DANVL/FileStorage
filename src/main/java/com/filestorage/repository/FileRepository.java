package com.filestorage.repository;

import com.filestorage.entities.File;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FileRepository extends ElasticsearchRepository<File,String> {
}
