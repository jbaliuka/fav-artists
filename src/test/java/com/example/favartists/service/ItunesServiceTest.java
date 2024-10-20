package com.example.favartists.service;

import com.example.favartists.model.Album;
import com.example.favartists.model.Artist;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
class ItunesServiceTest {

    @Test
    void searchArtists(){
        ItunesService itunesService = new ItunesService();
        List<Artist> results = itunesService.searchArtists("ABBA");
        assertFalse(results.isEmpty());
    }

    @Test
    void searchAlbums(){
        ItunesService itunesService = new ItunesService();
        List<Album> results = itunesService.searchAlbums(3492);
        assertFalse(results.isEmpty());
    }
}