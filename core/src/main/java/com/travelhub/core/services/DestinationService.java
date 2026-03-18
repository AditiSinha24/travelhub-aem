package com.travelhub.core.services;

import com.travelhub.core.models.DestinationModel;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

/**
 * OSGi Service interface for Destination business logic.
 *
 * Keeping business logic in a service (not in the Sling Model)
 * is a key AEM best practice — it makes testing easier and
 * keeps your models as pure data adapters.
 */
public interface DestinationService {

    /**
     * Returns all destinations under the TravelHub content root.
     *
     * @param resolver ResourceResolver from the current request
     * @return list of DestinationModel objects
     */
    List<DestinationModel> getAllDestinations(ResourceResolver resolver);

    /**
     * Returns destinations filtered by continent.
     *
     * @param resolver  ResourceResolver from the current request
     * @param continent e.g. "Asia", "Europe"
     * @return filtered list
     */
    List<DestinationModel> getDestinationsByContinent(ResourceResolver resolver, String continent);

    /**
     * Returns only destinations marked as featured.
     *
     * @param resolver ResourceResolver from the current request
     * @return featured destinations
     */
    List<DestinationModel> getFeaturedDestinations(ResourceResolver resolver);

    /**
     * Finds a single destination by its JCR path.
     *
     * @param resolver ResourceResolver from the current request
     * @param path     JCR path to the destination resource
     * @return DestinationModel or null if not found
     */
    DestinationModel getDestinationByPath(ResourceResolver resolver, String path);

}
