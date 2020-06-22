package com.itunes.integration.favorites.services;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itunes.integration.favorites.entities.FavoriteAlbums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

// Should have defined interface and be implementing it. Cutting on time.
@Service
@Scope("singleton")
public class CachingServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(CachingServiceImpl.class);

    private Cache<String, FavoriteAlbums> cache;

    public CachingServiceImpl() {
        cache = Caffeine.newBuilder().maximumSize(10000).build();
    }

    protected FavoriteAlbums checkInCache(String artistId) {
        FavoriteAlbums album = cache.getIfPresent(artistId);
        if (album != null) {
            logger.debug("Found in cache for artistId {}", artistId);
        } else {
            logger.debug("Nothing in case for artistId {}", artistId);
        }

        return album;
    }

    protected void saveToCache(FavoriteAlbums favoriteAlbum) {
        cache.put(favoriteAlbum.getArtistId(), favoriteAlbum);
    }

    protected Map<String, FavoriteAlbums> getOldestItems(int count) {
        return cache.policy().eviction().get().coldest(count);
    }

}
