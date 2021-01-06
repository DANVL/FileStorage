package com.filestorage.repository;

import com.filestorage.entities.File;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Set;

public interface FileRepository extends ElasticsearchRepository<File,String> {
    List<File> findAllByNameLikeAndTags(String name, Set<String> tags, Pageable pageable);
    List<File> findAllByNameLike(String name, Pageable pageable);
    long countAllByNameLike(String name);
    long countAllByNameLikeAndTags(String name, Set<String> tags);
}
