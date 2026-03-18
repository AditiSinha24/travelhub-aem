package com.travelhub.core.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.travelhub.core.models.DestinationModel;
import com.travelhub.core.services.DestinationService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * REST endpoint for destinations.
 *
 * URL pattern: /bin/travelhub/destinations
 *
 * Supported query params:
 *   ?continent=Asia       → filter by continent
 *   ?featured=true        → only featured destinations
 *
 * Example: http://localhost:4502/bin/travelhub/destinations?continent=Asia
 *
 * Note: /bin/* paths bypass Sling's default resource resolution,
 * making them ideal for API endpoints in AEM.
 */
@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/travelhub/destinations",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
        "sling.servlet.extensions=json"
    }
)
public class DestinationServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DestinationServlet.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Reference
    private DestinationService destinationService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String continent = request.getParameter("continent");
            String featuredParam = request.getParameter("featured");
            boolean onlyFeatured = "true".equalsIgnoreCase(featuredParam);

            List<DestinationModel> destinations;

            if (onlyFeatured) {
                destinations = destinationService.getFeaturedDestinations(request.getResourceResolver());
            } else if (continent != null && !continent.trim().isEmpty()) {
                destinations = destinationService.getDestinationsByContinent(
                    request.getResourceResolver(), continent);
            } else {
                destinations = destinationService.getAllDestinations(request.getResourceResolver());
            }

            ArrayNode resultsArray = MAPPER.createArrayNode();

            for (DestinationModel destination : destinations) {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("name", destination.getName());
                node.put("description", destination.getDescription());
                node.put("countryCode", destination.getCountryCode());
                node.put("continent", destination.getContinent());
                node.put("averagePricePerNight", destination.getAveragePricePerNight());
                node.put("heroImagePath", destination.getHeroImagePath());
                node.put("featured", destination.isFeatured());

                ArrayNode tagsArray = MAPPER.createArrayNode();
                destination.getTags().forEach(tagsArray::add);
                node.set("tags", tagsArray);

                resultsArray.add(node);
            }

            ObjectNode responseBody = MAPPER.createObjectNode();
            responseBody.put("total", destinations.size());
            responseBody.set("results", resultsArray);

            response.getWriter().write(MAPPER.writeValueAsString(responseBody));

        } catch (Exception e) {
            LOG.error("Error in DestinationServlet", e);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            ObjectNode errorNode = MAPPER.createObjectNode();
            errorNode.put("error", "An error occurred while fetching destinations");
            response.getWriter().write(MAPPER.writeValueAsString(errorNode));
        }
    }

}
