package com.travelhub.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DestinationModel.
 *
 * Adapts from a Sling Resource (Content Fragment node).
 * Uses DefaultInjectionStrategy.OPTIONAL so the model
 * never fails to adapt — gracefully handles missing properties.
 */
@Model(
    adaptables = Resource.class,
    adapters = DestinationModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = "travelhub/components/content/destination"
)
public class DestinationModelImpl implements DestinationModel {

    private static final Logger LOG = LoggerFactory.getLogger(DestinationModelImpl.class);

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String description;

    @ValueMapValue
    @Named("countryCode")
    private String countryCode;

    @ValueMapValue
    private String continent;

    @ValueMapValue
    @Named("averagePricePerNight")
    private double averagePricePerNight;

    @ValueMapValue
    private String[] tags;

    @ValueMapValue
    @Named("heroImagePath")
    private String heroImagePath;

    @ValueMapValue
    private boolean featured;

    @Override
    public String getName() {
        return Optional.ofNullable(name).orElse("");
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(description).orElse("");
    }

    @Override
    public String getCountryCode() {
        return Optional.ofNullable(countryCode).orElse("");
    }

    @Override
    public String getContinent() {
        return Optional.ofNullable(continent).orElse("");
    }

    @Override
    public double getAveragePricePerNight() {
        return averagePricePerNight;
    }

    @Override
    public List<String> getTags() {
        if (tags == null || tags.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(tags);
    }

    @Override
    public String getHeroImagePath() {
        return Optional.ofNullable(heroImagePath).orElse("");
    }

    @Override
    public boolean isFeatured() {
        return featured;
    }

}
