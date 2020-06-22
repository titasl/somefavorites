package com.itunes.integration.favorites.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class FavoriteAlbums {

    private String artistId;
    private List<Collection> albums;
    private LocalDateTime lastUpdated;
}
