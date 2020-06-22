package com.itunes.integration.favorites.controllers;

import com.itunes.integration.favorites.entities.FavoriteArtist;
import com.itunes.integration.favorites.services.FavoritesArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/favorite")
public class FavoriteArtistController {

    private static final Logger logger = LoggerFactory.getLogger(FavoriteArtistController.class);

    private FavoritesArtistService favoritesArtistService;

    @Value("${internal.artist.not.found.msg}")
    private String errorMsg;

    @Autowired
    public FavoriteArtistController(final FavoritesArtistService favoritesArtistService) {
        this.favoritesArtistService = favoritesArtistService;
    }

    @PutMapping("/{userId}/{artistId}")
    public void saveFavoriteArtist(final @PathVariable @NotNull String userId,
                             final @PathVariable @NotNull String artistId){
       favoritesArtistService.upsertFavoriteArtist(userId, artistId);
    }

    @GetMapping("/{userId}")
    public FavoriteArtist getFavoriteArtist(final @PathVariable @NotNull String userId) {
        try {
            FavoriteArtist favoriteArtist = favoritesArtistService.getFavoriteArtist(userId);
            if (favoriteArtist == null) {
                throw new NullPointerException("Null pointer");
            }
            return favoriteArtist;
        } catch (Exception ex) {
            logger.error("Encountered issue while retrieving favorite artist. Generating NOT FOUND answer.", ex);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, errorMsg, ex);
        }
    }
}
