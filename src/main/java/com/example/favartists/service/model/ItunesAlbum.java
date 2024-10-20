package com.example.favartists.service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItunesAlbum {
    private int artistId;
    private String collectionType;
    private String collectionName;
}
