package com.itunes.integration.favorites.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This class is intended to hold information about one collection.
 * Wrapper type will show if that is collection or not.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Collection {

    private String wrapperType;
    private int collectionId;
    private String collectionName;
    private String artistName;
    private double collectionPrice;
    private String currency;

}
