package com.itunes.integration.favorites.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class FavoriteAlbumsFromITunes {


    private int resultCount;
    private List<Collection> results;
}
