package com.aegershman.stlmpd.api;

import com.aegershman.stlmpd.geocoding.Position;
import com.aegershman.stlmpd.geocoding.GeocodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceCallService {

    private final ServiceCallRepository repository;
    private final GeocodingService geocodingService;

    public List<ServiceCall> saveAll(List<ServiceCall> serviceCalls) {
        List<ServiceCall> savedCalls = new ArrayList<>(serviceCalls.size());
        for (ServiceCall serviceCall : serviceCalls) {
            var saved = save(serviceCall);
            savedCalls.add(saved);
        }
        return savedCalls;
    }

    public ServiceCall save(ServiceCall serviceCall) {
        if (repository.existsByServiceCallId(serviceCall.getServiceCallId())) {
            return repository.findByServiceCallId(serviceCall.getServiceCallId());
        } else {
            getPositionGPS(serviceCall);
            return repository.save(serviceCall);
        }
    }

    private void getPositionGPS(ServiceCall serviceCall) {
        String address = serviceCall.getAddress();
        address = address.replace("XX", "00");
        var addressQuery = address + " St. Louis MO";
        Position position = geocodingService.geocodeAddressToGPS(addressQuery);
        if (position == null) {
            log.info("No GPS location found for address %s".formatted(addressQuery));
        } else {
            serviceCall.setLongitude(position.getLongitude());
            serviceCall.setLatitude(position.getLatitude());
        }
    }

    public Page<ServiceCall> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ServiceCall> findAllWithCallTimeAfter(TimeSinceField timeSinceField, Pageable pageable) {
        // todo
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTimeAgo = null;
        switch (timeSinceField) {
            case FIFTEEN_MIN -> dateTimeAgo = now.minusMinutes(15);
            case TWO_HOURS -> dateTimeAgo = now.minusHours(2);
            case ANY -> dateTimeAgo = null;
        }

        if (dateTimeAgo == null) {
            return repository.findAll(pageable);
        } else {
            return repository.findByCallTimeLessThan(dateTimeAgo, pageable);
        }
    }

}
