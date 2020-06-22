package com.itunes.integration.favorites.services;

import com.itunes.integration.favorites.dao.FavoriteArtistRepository;
import com.itunes.integration.favorites.entities.FavoriteArtist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritesArtistServiceImpl implements FavoritesArtistService {
    private static final Logger logger = LoggerFactory.getLogger(FavoritesArtistService.class);
    private FavoriteArtistRepository favoriteArtistRepository;

    @Autowired
    public FavoritesArtistServiceImpl(final FavoriteArtistRepository repository) {
        favoriteArtistRepository = repository;
    }

    /**
     * Upsert operation for handling favorite artist. Insert if no such exists, update if already exists.
     * @param userId    User id
     * @param artistId  Artist Id
     * @return Created or updated object
     */
    @Override
    public FavoriteArtist upsertFavoriteArtist(final String userId, final String artistId) {
        FavoriteArtist favoriteArtist = favoriteArtistRepository.findByUserId(userId);
        if (favoriteArtist == null) {
            logger.debug("This user {} doesn'thave favorite artist yet, create a new entry with artist ID {}", userId, artistId);
            favoriteArtist = new FavoriteArtist(userId, artistId);
        } else {
            logger.debug("User with ID {} already had previously selected favorite artist, replacing it with artist ID {}", userId, artistId);
            favoriteArtist.setArtistId(artistId);
        }

        return favoriteArtistRepository.save(favoriteArtist);
    }

    /**
     * Retrieve stored favorite artist for the user
     * @param userId User Id to retrieve for
     * @return Full object if found, null otherwise
     */
    @Override
    public FavoriteArtist getFavoriteArtist(final String userId) {
        return favoriteArtistRepository.findByUserId(userId);
    }
}
