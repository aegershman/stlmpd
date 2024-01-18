package com.aegershman.stlmpd.geocode;

import com.aegershman.stlmpd.StlmpdProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final StlmpdProperties properties;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public Position geocodeAddressToGPS(String streetAddress) {
        String formattedURL = String.format("%s/geocode?q=%s&apiKey=%s",
                properties.getGeocodeApiUrlBase(),
                streetAddress.replace(' ', '+'),
                properties.getGeocodeApiToken());

        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(formattedURL)).build();
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            Items items = objectMapper.readValue(httpResponse.body(), Items.class);

            return (items != null && items.getItems() != null) ? items.getItems().get(0).getPosition() : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}