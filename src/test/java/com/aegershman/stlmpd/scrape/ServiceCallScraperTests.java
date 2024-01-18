package com.aegershman.stlmpd.scrape;

import com.aegershman.stlmpd.api.ServiceCall;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

public class ServiceCallScraperTests {

    @Spy
    @InjectMocks
    private ServiceCallScraper serviceCallScraper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void scrapeReturnsExpectedServiceCalls() throws IOException {
        Document doc = Jsoup.parse("<table id=\"gvData\"><tbody><tr><td>2022-01-01 00:00:00</td><td>123</td><td>Address</td><td>Reason</td></tr></tbody></table>");
        doReturn(doc).when(serviceCallScraper).getDocument();

        List<ServiceCall> serviceCalls = serviceCallScraper.scrape();

        assertEquals(1, serviceCalls.size());
        ServiceCall serviceCall = serviceCalls.get(0);
        assertEquals("123", serviceCall.getServiceCallId());
        assertEquals("Address", serviceCall.getAddress());
        assertEquals("Reason", serviceCall.getReason());
    }

    @Test
    public void scrapeReturnsEmptyListWhenNoServiceCalls() throws IOException {
        Document doc = Jsoup.parse("<table id=\"gvData\"><tbody></tbody></table>");
        doReturn(doc).when(serviceCallScraper).getDocument();

        List<ServiceCall> serviceCalls = serviceCallScraper.scrape();

        assertEquals(0, serviceCalls.size());
    }

}