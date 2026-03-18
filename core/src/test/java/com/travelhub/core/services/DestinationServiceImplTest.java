package com.travelhub.core.services;

import com.travelhub.core.models.DestinationModel;
import com.travelhub.core.models.DestinationModelImpl;
import com.travelhub.core.services.impl.DestinationServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class DestinationServiceImplTest {

    private final AemContext context = new AemContext();
    private DestinationService destinationService;
    private static final String ROOT_PATH = "/content/dam/travelhub/destinations";

    @BeforeEach
    void setUp() throws Exception {
        context.addModelsForClasses(DestinationModelImpl.class);

        // Manually instantiate — avoids NoScrMetadataException
        DestinationServiceImpl impl = new DestinationServiceImpl();
        Field rootPathField = DestinationServiceImpl.class.getDeclaredField("destinationsRootPath");
        rootPathField.setAccessible(true);
        rootPathField.set(impl, ROOT_PATH);
        destinationService = impl;

        context.create().resource(ROOT_PATH);

        context.create().resource(ROOT_PATH + "/bali", Map.of(
                "sling:resourceType", "travelhub/components/content/destination",
                "name", "Bali, Indonesia",
                "continent", "Asia",
                "countryCode", "ID",
                "averagePricePerNight", 75.0,
                "featured", true,
                "tags", new String[]{"beach", "culture"}
        ));

        context.create().resource(ROOT_PATH + "/paris", Map.of(
                "sling:resourceType", "travelhub/components/content/destination",
                "name", "Paris, France",
                "continent", "Europe",
                "countryCode", "FR",
                "averagePricePerNight", 180.0,
                "featured", false,
                "tags", new String[]{"city", "culture", "food"}
        ));

        context.create().resource(ROOT_PATH + "/tokyo", Map.of(
                "sling:resourceType", "travelhub/components/content/destination",
                "name", "Tokyo, Japan",
                "continent", "Asia",
                "countryCode", "JP",
                "averagePricePerNight", 130.0,
                "featured", true,
                "tags", new String[]{"city", "technology", "food"}
        ));
    }

    @Test
    @DisplayName("getAllDestinations should return all 3 destinations")
    void testGetAllDestinations() {
        List<DestinationModel> results = destinationService.getAllDestinations(context.resourceResolver());
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("getDestinationsByContinent should filter correctly")
    void testGetByContinent() {
        List<DestinationModel> asiaResults = destinationService
                .getDestinationsByContinent(context.resourceResolver(), "Asia");
        assertEquals(2, asiaResults.size());

        List<DestinationModel> europeResults = destinationService
                .getDestinationsByContinent(context.resourceResolver(), "Europe");
        assertEquals(1, europeResults.size());
        assertEquals("Paris, France", europeResults.get(0).getName());
    }

    @Test
    @DisplayName("getFeaturedDestinations should return only featured")
    void testGetFeaturedDestinations() {
        List<DestinationModel> featured = destinationService
                .getFeaturedDestinations(context.resourceResolver());
        assertEquals(2, featured.size());
        assertTrue(featured.stream().allMatch(DestinationModel::isFeatured));
    }

    @Test
    @DisplayName("getDestinationByPath should return correct destination")
    void testGetByPath() {
        DestinationModel bali = destinationService
                .getDestinationByPath(context.resourceResolver(), ROOT_PATH + "/bali");
        assertNotNull(bali);
        assertEquals("Bali, Indonesia", bali.getName());
    }

    @Test
    @DisplayName("getDestinationByPath should return null for invalid path")
    void testGetByInvalidPath() {
        DestinationModel result = destinationService
                .getDestinationByPath(context.resourceResolver(), "/invalid/path");
        assertNull(result);
    }

    @Test
    @DisplayName("getDestinationsByContinent with null should return all")
    void testGetByContinentWithNull() {
        List<DestinationModel> results = destinationService
                .getDestinationsByContinent(context.resourceResolver(), null);
        assertEquals(3, results.size());
    }
}