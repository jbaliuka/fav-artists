package com.example.favartists.api;

import com.example.favartists.model.Album;
import com.example.favartists.model.Artist;
import com.example.favartists.repository.ArtistRepository;
import com.example.favartists.service.ItunesService;
import lombok.AllArgsConstructor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(name = "/", produces = MimeTypeUtils.APPLICATION_JSON_VALUE,
        consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class FavArtistsController {
    private final ArtistRepository artistRepository;
    private final ItunesService itunesService;

    public FavArtistsController(ArtistRepository artistRepository, ItunesService itunesService){
        this.artistRepository = artistRepository;
        this.itunesService = itunesService;
    }
    @GetMapping("artists")
    public List<Artist> getFavoriteArtists(@RequestHeader("userId") String userId) {
        return artistRepository.getArtists(userId);
    }

    @PutMapping("artists")
    public Artist saveArtist(@RequestBody Artist artist, @RequestHeader("userId") String userId) {
        artistRepository.saveArtist(artist, userId);
        return artist;
    }

    @GetMapping("artists/search")
    public List<Artist> searchArtists(@RequestParam("term") String term) {
        return itunesService.searchArtists(term);
    }
    @GetMapping("/{artistId}/albums")
    public List<Album> getArtistAlbums(@PathVariable("artistId") Integer artistId) {
        return itunesService.searchAlbums(artistId);
    }
}
