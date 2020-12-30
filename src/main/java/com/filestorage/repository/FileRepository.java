package com.filestorage.repository;

import com.filestorage.entities.File;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Set;

public interface FileRepository extends ElasticsearchRepository<File,String> {
    List<File> findAllByTags(Set<String> tags, Pageable pageable);
}
