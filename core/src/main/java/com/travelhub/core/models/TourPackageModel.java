package com.travelhub.core.models;

/**
 * Sling Model interface for a Tour Package Content Fragment.
 * A tour package links a destination with pricing and duration.
 */
public interface TourPackageModel {

    String getTitle();

    String getDestinationPath();

    int getDurationDays();

    double getPriceUsd();

    String getDifficultyLevel();  // EASY, MODERATE, CHALLENGING

    int getMaxGroupSize();

    boolean isAvailable();

    String getHighlights();

}
