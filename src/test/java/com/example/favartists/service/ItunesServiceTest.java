package com.example.favartists.service;

import com.example.favartists.model.Album;
import com.example.favartists.model.Artist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(properties =
        {"itunes.artists.url=https://itunes.apple.com/search?entity=allArtist&term=%s",
                "itunes.albums.url=https://itunes.apple.com/lookup?amgArtistId=%s&entity=album&limit=5"})
class ItunesServiceTest {

    @Autowired
    private ItunesService itunesService;

    @Test
    void searchArtists(){
        List<Artist> results = itunesService.searchArtists("ABBA");
        assertFalse(results.isEmpty());
    }

    @Test
    void searchAlbums(){
        List<Album> results = itunesService.searchAlbums(3492);
        assertFalse(results.isEmpty());
    }
}