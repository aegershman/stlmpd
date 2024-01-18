package com.aegershman.stlmpd.geocode;

import com.aegershman.stlmpd.StlmpdProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class GeocodingService {

    private final StlmpdProperties properties;

    public Position geocodeAddressToGPS(String streetAddress) {
        String apiKey = properties.getGeocodeApiToken();
        String URL = "%s/geocode?q=%s&apiKey=%s";
        String address = streetAddress.replace(' ', '+');
        String formattedURL = URL.formatted(properties.getGeocodeApiUrlBase(), address, apiKey);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder().uri(URI.create(formattedURL)).build();
        } catch (IllegalArgumentException e) {
            // todo
            return null;
        }
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String body = httpResponse.body();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            Items items = objectMapper.readValue(body, Items.class);
            if (items == null || items.getItems() == null) {
                // todo
                return null;
            } else {
                return items.getItems().get(0).getPosition();
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
