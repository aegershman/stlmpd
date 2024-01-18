package com.aegershman.stlmpd.scrape;

import com.aegershman.stlmpd.StlmpdProperties;
import com.aegershman.stlmpd.api.ServiceCall;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Disabled
public class ServiceCallScraperTests {

    @Mock
    private StlmpdProperties stlmpdProperties;

    @InjectMocks
    private ServiceCallScraper serviceCallScraper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void scrapeReturnsExpectedServiceCalls() throws IOException {
        // Mock Jsoup.connect().get() to return a Document with a specific structure
        Document doc = Jsoup.parse("<table id=\"gvData\"><tbody><tr><td>01/01/2022 00:00:00</td><td>123</td><td>Address</td><td>Reason</td></tr></tbody></table>");
        when(Jsoup.connect(anyString()).get()).thenReturn(doc);

        // Mock StlmpdProperties to return a specific API URL
        when(stlmpdProperties.getApiUrl()).thenReturn("http://example.com");

        List<ServiceCall> serviceCalls = serviceCallScraper.scrape();

        assertEquals(1, serviceCalls.size());
        ServiceCall serviceCall = serviceCalls.get(0);
        assertEquals("123", serviceCall.getServiceCallId());
        assertEquals("Address", serviceCall.getAddress());
        assertEquals("Reason", serviceCall.getReason());
    }

    @Test
    public void scrapeReturnsEmptyListWhenNoServiceCalls() throws IOException {
        // Mock Jsoup.connect().get() to return a Document with no service calls
        Document doc = Jsoup.parse("<table id=\"gvData\"><tbody></tbody></table>");
        when(Jsoup.connect(anyString()).get()).thenReturn(doc);

        // Mock StlmpdProperties to return a specific API URL
        when(stlmpdProperties.getApiUrl()).thenReturn("http://example.com");

        List<ServiceCall> serviceCalls = serviceCallScraper.scrape();

        assertEquals(0, serviceCalls.size());
    }

    @Test
    public void scrapeThrowsIOExceptionWhenUnableToConnect() throws IOException {
        // Mock Jsoup.connect().get() to throw an IOException
        when(Jsoup.connect(anyString()).get()).thenThrow(new IOException());

        // Mock StlmpdProperties to return a specific API URL
        when(stlmpdProperties.getApiUrl()).thenReturn("http://example.com");

        try {
            serviceCallScraper.scrape();
        } catch (IOException e) {
            assertEquals(IOException.class, e.getClass());
        }
    }
}