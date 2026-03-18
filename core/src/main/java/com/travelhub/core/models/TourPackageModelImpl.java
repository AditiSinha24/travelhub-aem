package com.travelhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Named;
import java.util.Optional;

@Model(
    adaptables = Resource.class,
    adapters = TourPackageModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = "travelhub/components/content/tourpackage"
)
public class TourPackageModelImpl implements TourPackageModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    @Named("destinationPath")
    private String destinationPath;

    @ValueMapValue
    @Named("durationDays")
    private int durationDays;

    @ValueMapValue
    @Named("priceUsd")
    private double priceUsd;

    @ValueMapValue
    @Named("difficultyLevel")
    private String difficultyLevel;

    @ValueMapValue
    @Named("maxGroupSize")
    private int maxGroupSize;

    @ValueMapValue
    private boolean available;

    @ValueMapValue
    private String highlights;

    @Override
    public String getTitle() {
        return Optional.ofNullable(title).orElse("");
    }

    @Override
    public String getDestinationPath() {
        return Optional.ofNullable(destinationPath).orElse("");
    }

    @Override
    public int getDurationDays() {
        return durationDays;
    }

    @Override
    public double getPriceUsd() {
        return priceUsd;
    }

    @Override
    public String getDifficultyLevel() {
        return Optional.ofNullable(difficultyLevel).orElse("EASY");
    }

    @Override
    public int getMaxGroupSize() {
        return maxGroupSize > 0 ? maxGroupSize : 10;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getHighlights() {
        return Optional.ofNullable(highlights).orElse("");
    }

}
