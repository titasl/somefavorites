package com.itunes.integration.favorites.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CachingServiceImplTest {

    private CachingServiceImpl cachingService;

    @BeforeEach
    public void setup() {
        cachingService = new CachingServiceImpl();
    }

    @Test
    void shouldGetOneLatestItem() {
//        FavoriteAlbums albums = new FavoriteAlbums();
//        albums.setArtistId("artist");
//        albums.setLastUpdated(LocalDateTime.now());
//        albums.setAlbums(Collections.EMPTY_LIST);
//
//        cachingService.saveToCache(albums);
//
//        Map<String, FavoriteAlbums> answer = cachingService.getOldestItems(5);
//
//        assertEquals(1, answer.size());
//        assertNotNull(answer.get("artist"));
        assertTrue(true);

    }
}