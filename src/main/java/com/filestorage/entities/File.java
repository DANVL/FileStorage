package com.filestorage.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.Set;

@Document(indexName = "storage")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    @Id
    private String id;
    private String name;
    private int size;
    private Set<String> tags;

    public void addTags(List<String> tags){
        this.tags.addAll(tags);
    }

    public boolean removeTags(List<String> tags){
        if(this.tags.containsAll(tags)){
            return this.tags.removeAll(tags);
        }

        return false;
    }
}
