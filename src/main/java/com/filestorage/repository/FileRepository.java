package com.filestorage.repository;

import com.filestorage.entities.File;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.awt.print.Pageable;
import java.util.Set;

public interface FileRepository extends ElasticsearchRepository<File,String> {
    Iterable<File> findByTagsContains(Set<String> tags, Pageable pageable);
}
