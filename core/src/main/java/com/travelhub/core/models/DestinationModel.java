package com.travelhub.core.models;

import java.util.List;

/**
 * Sling Model interface for a Travel Destination.
 * Represents a Content Fragment of type "destination".
 *
 * Keeping interface separate from impl is a best practice
 * for AEM — it allows clean unit testing and future flexibility.
 */
public interface DestinationModel {

    /**
     * @return the destination name e.g. "Bali, Indonesia"
     */
    String getName();

    /**
     * @return short description for listing pages
     */
    String getDescription();

    /**
     * @return ISO country code e.g. "ID"
     */
    String getCountryCode();

    /**
     * @return continent name for regional filtering
     */
    String getContinent();

    /**
     * @return average price per night in USD
     */
    double getAveragePricePerNight();

    /**
     * @return list of tags e.g. ["beach", "adventure", "family"]
     */
    List<String> getTags();

    /**
     * @return path to the hero image in DAM
     */
    String getHeroImagePath();

    /**
     * @return true if this destination is currently featured
     */
    boolean isFeatured();

}
