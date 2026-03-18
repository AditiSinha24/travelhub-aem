package com.travelhub.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DestinationModelImpl.
 *
 * Uses io.wcm AEM Mocks — no real AEM instance needed.
 * AemContext simulates a full Sling/JCR environment in memory.
 *
 * This is the kind of test that impresses international interviewers —
 * most AEM devs skip unit testing entirely.
 */
@ExtendWith(AemContextExtension.class)
class DestinationModelImplTest {

    private final AemContext context = new AemContext();

    private static final String DESTINATION_PATH = "/content/travelhub/destinations/bali";

    @BeforeEach
    void setUp() {
        // Register the Sling Model so AemContext knows about it
        context.addModelsForClasses(DestinationModelImpl.class);

        // Create a mock resource with properties — simulates a Content Fragment node
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "Bali, Indonesia");
        properties.put("description", "A tropical paradise with stunning temples and rice terraces.");
        properties.put("countryCode", "ID");
        properties.put("continent", "Asia");
        properties.put("averagePricePerNight", 75.0);
        properties.put("tags", new String[]{"beach", "culture", "adventure"});
        properties.put("heroImagePath", "/content/dam/travelhub/images/bali-hero.jpg");
        properties.put("featured", true);
        properties.put("sling:resourceType", "travelhub/components/content/destination");

        context.create().resource(DESTINATION_PATH, properties);
    }

    @Test
    @DisplayName("Should correctly adapt resource to DestinationModel")
    void testModelAdaptation() {
        Resource resource = context.resourceResolver().getResource(DESTINATION_PATH);
        assertNotNull(resource, "Resource should exist at the given path");

        DestinationModel model = resource.adaptTo(DestinationModel.class);
        assertNotNull(model, "Resource should adapt to DestinationModel");
    }

    @Test
    @DisplayName("Should return correct destination name")
    void testGetName() {
        DestinationModel model = getModel();
        assertEquals("Bali, Indonesia", model.getName());
    }

    @Test
    @DisplayName("Should return correct country code")
    void testGetCountryCode() {
        DestinationModel model = getModel();
        assertEquals("ID", model.getCountryCode());
    }

    @Test
    @DisplayName("Should return correct continent")
    void testGetContinent() {
        DestinationModel model = getModel();
        assertEquals("Asia", model.getContinent());
    }

    @Test
    @DisplayName("Should return correct average price")
    void testGetAveragePricePerNight() {
        DestinationModel model = getModel();
        assertEquals(75.0, model.getAveragePricePerNight());
    }

    @Test
    @DisplayName("Should return tags as a list")
    void testGetTags() {
        DestinationModel model = getModel();
        assertNotNull(model.getTags());
        assertEquals(3, model.getTags().size());
        assertTrue(model.getTags().contains("beach"));
        assertTrue(model.getTags().contains("culture"));
        assertTrue(model.getTags().contains("adventure"));
    }

    @Test
    @DisplayName("Should return true for featured destination")
    void testIsFeatured() {
        DestinationModel model = getModel();
        assertTrue(model.isFeatured());
    }

    @Test
    @DisplayName("Should return empty string for missing properties instead of null")
    void testNullSafety() {
        // Create a resource with no properties
        context.create().resource("/content/travelhub/destinations/empty",
            Map.of("sling:resourceType", "travelhub/components/content/destination"));

        Resource resource = context.resourceResolver().getResource("/content/travelhub/destinations/empty");
        DestinationModel model = resource.adaptTo(DestinationModel.class);

        assertNotNull(model);
        assertEquals("", model.getName(),          "Name should default to empty string");
        assertEquals("", model.getCountryCode(),   "CountryCode should default to empty string");
        assertEquals("", model.getContinent(),     "Continent should default to empty string");
        assertTrue(model.getTags().isEmpty(),       "Tags should default to empty list");
    }

    // Helper to get the model for the main test resource
    private DestinationModel getModel() {
        Resource resource = context.resourceResolver().getResource(DESTINATION_PATH);
        assertNotNull(resource);
        DestinationModel model = resource.adaptTo(DestinationModel.class);
        assertNotNull(model);
        return model;
    }

}
