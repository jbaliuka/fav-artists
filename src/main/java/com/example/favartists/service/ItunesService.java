package com.example.favartists.service;

import com.example.favartists.model.Album;
import com.example.favartists.model.Artist;
import com.example.favartists.service.model.ItunesAlbumResponse;
import com.example.favartists.service.model.ItunesArtistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ItunesService {

    public static final String ALBUM_TYPE = "Album";
    private final ConcurrentHashMap<String, List<Artist>> artistsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, List<Album>> albumsCache = new ConcurrentHashMap<>();

    @Value("${itunes.artists.url}") String artistsUrl;
    @Value("${itunes.albums.url}") String albumsUrl;

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    @SneakyThrows
    public List<Artist> searchArtists(String term) {
        List<Artist> result = artistsCache.get(term);
        if (result != null) {
            return result;
        }
        HttpRequest searchRequest = HttpRequest.newBuilder(URI.create(artistsUrl.formatted(term))).GET().build();
        try (InputStream body = httpClient.send(searchRequest, HttpResponse.BodyHandlers.ofInputStream()).body()) {
            result = new ObjectMapper().readValue(body, ItunesArtistResponse.class)
                    .getResults()
                    .stream()
                    .map(artist -> {
                        var favoriteArtist = new Artist();
                        favoriteArtist.setArtistName(artist.getArtistName());
                        favoriteArtist.setArtistId(artist.getArtistId());
                        return favoriteArtist;
                    }).toList();
            artistsCache.put(term, result);
            return result;
        }
    }

    @SneakyThrows
    public List<Album> searchAlbums(Integer artistId) {
        List<Album> result = albumsCache.get(artistId);
        if (result != null) {
            return result;
        }
        HttpRequest searchRequest = HttpRequest.newBuilder(URI.create(albumsUrl.formatted(artistId.toString()))).GET().build();
        try (InputStream body = httpClient.send(searchRequest, HttpResponse.BodyHandlers.ofInputStream()).body()) {
            result = new ObjectMapper().readValue(body, ItunesAlbumResponse.class)
                    .getResults()
                    .stream()
                    .filter(album -> ALBUM_TYPE.equals(album.getCollectionType()))
                    .map(album -> {
                        var favoriteAlbum = new Album();
                        favoriteAlbum.setAlbumName(album.getCollectionName());
                        favoriteAlbum.setArtistId(album.getArtistId());
                        return favoriteAlbum;
                    })
                    .toList();
            albumsCache.put(artistId, result);
            return result;
        }
    }
}
