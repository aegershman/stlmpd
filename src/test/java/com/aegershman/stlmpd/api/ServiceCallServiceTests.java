package com.aegershman.stlmpd.api;

import com.aegershman.stlmpd.geocode.GeocodingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ServiceCallServiceTests {

    @Mock
    private ServiceCallRepository repository;

    @Mock
    private GeocodingService geocodingService;

    private ServiceCallService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ServiceCallService(repository, geocodingService);
    }

    @Test
    public void deleteAll_deletesWhenCountIsGreaterThan9500() {
        when(repository.count()).thenReturn(9501L);

        service.deleteAll();

        verify(repository).deleteAll();
    }

    @Test
    public void deleteAll_doesNotDeleteWhenCountIsLessThan9500() {
        when(repository.count()).thenReturn(9500L);

        service.deleteAll();

        verify(repository, never()).deleteAll();
    }
}