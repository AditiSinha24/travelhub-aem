package com.travelhub.core.services.impl;

import com.travelhub.core.models.DestinationModel;
import com.travelhub.core.services.DestinationService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DestinationService.
 *
 * Annotated with @Component so OSGi registers it automatically.
 * @Designate links it to an OSGi config — this lets you change
 * the content root path per environment without redeployment.
 */
@Component(service = DestinationService.class, immediate = true)
@Designate(ocd = DestinationServiceImpl.Config.class)
public class DestinationServiceImpl implements DestinationService {

    private static final Logger LOG = LoggerFactory.getLogger(DestinationServiceImpl.class);

    private String destinationsRootPath;

    /**
     * OSGi Configuration for this service.
     * Editable at: http://localhost:4502/system/console/configMgr
     */
    @ObjectClassDefinition(name = "TravelHub - Destination Service Configuration")
    public @interface Config {

        @AttributeDefinition(
            name = "Destinations Root Path",
            description = "JCR path where Destination Content Fragments are stored"
        )
        String destinations_root_path() default "/content/dam/travelhub/destinations";

    }

    @Activate
    @Modified
    protected void activate(Config config) {
        this.destinationsRootPath = config.destinations_root_path();
        LOG.info("DestinationService activated. Root path: {}", destinationsRootPath);
    }

    @Override
    public List<DestinationModel> getAllDestinations(ResourceResolver resolver) {
        if (resolver == null) {
            LOG.warn("ResourceResolver is null — returning empty list");
            return Collections.emptyList();
        }

        Resource rootResource = resolver.getResource(destinationsRootPath);
        if (rootResource == null) {
            LOG.warn("Destinations root not found at: {}", destinationsRootPath);
            return Collections.emptyList();
        }

        List<DestinationModel> destinations = new ArrayList<>();
        Iterator<Resource> children = rootResource.listChildren();

        while (children.hasNext()) {
            Resource child = children.next();
            DestinationModel model = child.adaptTo(DestinationModel.class);
            if (model != null) {
                destinations.add(model);
            }
        }

        LOG.debug("Found {} destinations", destinations.size());
        return destinations;
    }

    @Override
    public List<DestinationModel> getDestinationsByContinent(ResourceResolver resolver, String continent) {
        if (continent == null || continent.trim().isEmpty()) {
            return getAllDestinations(resolver);
        }

        return getAllDestinations(resolver).stream()
            .filter(d -> continent.equalsIgnoreCase(d.getContinent()))
            .collect(Collectors.toList());
    }

    @Override
    public List<DestinationModel> getFeaturedDestinations(ResourceResolver resolver) {
        return getAllDestinations(resolver).stream()
            .filter(DestinationModel::isFeatured)
            .collect(Collectors.toList());
    }

    @Override
    public DestinationModel getDestinationByPath(ResourceResolver resolver, String path) {
        if (resolver == null || path == null) {
            return null;
        }

        Resource resource = resolver.getResource(path);
        if (resource == null) {
            LOG.warn("No resource found at path: {}", path);
            return null;
        }

        return resource.adaptTo(DestinationModel.class);
    }

}
