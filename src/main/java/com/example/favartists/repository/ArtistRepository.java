package com.example.favartists.repository;

import com.example.favartists.model.Artist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ArtistRepository {
    ConcurrentHashMap<Integer, Artist> artists = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> favoriteArtists = new ConcurrentHashMap<>();

    public List<Artist> getArtists(String userId) {
        return favoriteArtists.entrySet()
                .stream().filter(entry -> entry.getKey().equals(userId))
                .map(Map.Entry::getValue)
                .map(artisId -> Optional.ofNullable(artists.get(artisId)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public void saveArtist(Artist artist, String userId) {
        favoriteArtists.put(userId, artist.getArtistId());
        artists.put(artist.getArtistId(), artist);
    }
}
