package com.itunes.integration.favorites.services;

import com.itunes.integration.favorites.entities.FavoriteArtist;

/**
 *  Defines functionality for handling favorite artist
 */
public interface FavoritesArtistService {

    /**
     * Store information about the selected artist
     * @param userId User to store to
     * @param artistId Selected artist Id
     * @return created object in DB
     */
    FavoriteArtist upsertFavoriteArtist(final String userId, final String artistId);

    /**
     * Find information about stored selected artist
     * @param userId User to find for
     * @return null if no stored, of full information if found
     */
    FavoriteArtist getFavoriteArtist(final String userId);
}
