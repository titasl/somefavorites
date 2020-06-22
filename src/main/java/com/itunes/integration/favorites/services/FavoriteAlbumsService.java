package com.itunes.integration.favorites.services;

import com.itunes.integration.favorites.entities.FavoriteAlbums;

/**
 * Methods collection to handling everything related with albums retrieval
 */
public interface FavoriteAlbumsService {

    /**
     * Find albums for favorite artist which was saved for that user
     * @param userId User to look upon
     * @return Albums if found, or null otherwise
     */
    FavoriteAlbums getFavoriteAlbums(String userId);
}
