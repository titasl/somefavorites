package com.itunes.integration.favorites.services;

import com.itunes.integration.favorites.dao.FavoriteArtistRepository;
import com.itunes.integration.favorites.entities.Collection;
import com.itunes.integration.favorites.entities.FavoriteAlbums;
import com.itunes.integration.favorites.entities.FavoriteAlbumsFromITunes;
import com.itunes.integration.favorites.entities.FavoriteArtist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteAlbumsServiceImpl implements FavoriteAlbumsService {

    private static final String COLLECTION = "collection";

    private static final Logger logger = LoggerFactory.getLogger(FavoriteAlbumsServiceImpl.class);

    @Value("${itunes.favorite.url}")
    private String favoriteAlbumsUrl;

    @Value("${itunes.items.to.refresh}")
    private int howManyToUpdateOnEachRun;

    private FavoriteArtistRepository favoriteArtistRepository;

    private RestTemplate restTemplate;

    private CachingServiceImpl cachingService;

    @Autowired
    public FavoriteAlbumsServiceImpl(final FavoriteArtistRepository repository, final RestTemplateBuilder restTemplateBuilder,
                                     final CachingServiceImpl cachingService) {
        this.favoriteArtistRepository = repository;

        this.restTemplate = restTemplateBuilder.build();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        this.restTemplate.setMessageConverters(messageConverters);

        this.cachingService = cachingService;
    }

    public FavoriteAlbumsServiceImpl(final FavoriteArtistRepository repository, final RestTemplate restTemplate,
                                     final CachingServiceImpl cachingService) {
        favoriteArtistRepository = repository;
        this.restTemplate = restTemplate;
        this.cachingService = cachingService;
    }

    @Override
    public FavoriteAlbums getFavoriteAlbums(final String userId) {
        FavoriteArtist favoriteArtist = favoriteArtistRepository.findByUserId(userId);
        String artistId = favoriteArtist.getArtistId();
        FavoriteAlbums favoriteAlbums = cachingService.checkInCache(artistId);
        if (favoriteAlbums != null) {
            return favoriteAlbums;
        }

        FavoriteAlbumsFromITunes favoriteAlbumsFromITunes = getAlbumsFromITunes(artistId);
        favoriteAlbums = processFavoriteAlbumsAnswer(favoriteAlbumsFromITunes, artistId);
        return favoriteAlbums;
    }


    private FavoriteAlbums processFavoriteAlbumsAnswer(FavoriteAlbumsFromITunes items, String artistId) {
        List<Collection> albums = new LinkedList<>();
        for (Collection item : items.getResults()) {
            if (COLLECTION.equals(item.getWrapperType())) {
                albums.add(item);
            }
        }

        FavoriteAlbums result = new FavoriteAlbums();
        result.setAlbums(albums);
        result.setArtistId(artistId);
        result.setLastUpdated(LocalDateTime.now());

        cachingService.saveToCache(result);

        return result;
    }

    protected FavoriteAlbumsFromITunes getAlbumsFromITunes(String artistId) {
        String fullUrl = String.format(favoriteAlbumsUrl, artistId);
        FavoriteAlbumsFromITunes favoriteAlbumsFromITunes = restTemplate.postForObject(fullUrl, null,
                FavoriteAlbumsFromITunes.class);

        return favoriteAlbumsFromITunes;
    }


    // Should be in the separate service due Single Responsibility but this will be enough for this example
    @Scheduled(fixedRate = 60000)
    public void refreshSomeCacheItems() {
        Map<String, FavoriteAlbums> itemsToUpdate = cachingService.getOldestItems(howManyToUpdateOnEachRun);
        for (FavoriteAlbums album : itemsToUpdate.values()) {
            try {
                String artistId = album.getArtistId();
                logger.debug("Updating cached information for artistId {}!", artistId);
                FavoriteAlbumsFromITunes favoriteAlbumsFromITunes = getAlbumsFromITunes(artistId);
                processFavoriteAlbumsAnswer(favoriteAlbumsFromITunes, artistId);
            } catch (
                    Exception ex) {
                logger.error("Encountered error while updating items", ex);
            }
        }

    }
}
