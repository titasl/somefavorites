package com.itunes.integration.favorites.controllers;

import com.itunes.integration.favorites.entities.FavoriteAlbums;
import com.itunes.integration.favorites.services.FavoriteAlbumsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/albums")
public class FiveFavoriteAlbumsController {

    private static final Logger logger = LoggerFactory.getLogger(FiveFavoriteAlbumsController.class);

    private FavoriteAlbumsService favoriteAlbumsService;

    @Value("${itunes.not.found.msg}")
    private String errorMsg;

    @Autowired
    public FiveFavoriteAlbumsController(final FavoriteAlbumsService favoriteAlbumsService) {
        this.favoriteAlbumsService = favoriteAlbumsService;
    }

    @GetMapping("/{userId}")
    public FavoriteAlbums getFavoriteAlbums(final @PathVariable @NotNull String userId) {
        try {
            return favoriteAlbumsService.getFavoriteAlbums(userId);
        } catch (Exception ex) {
            logger.error("Encountered issue while retrieving albums. Generating NOT FOUND answer.", ex);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, errorMsg, ex);
        }

    }
}
