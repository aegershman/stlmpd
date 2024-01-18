package com.aegershman.stlmpd.api;

import com.aegershman.stlmpd.geocode.GeocodingService;
import com.aegershman.stlmpd.geocode.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceCallService {

    private final ServiceCallRepository repository;
    private final GeocodingService geocodingService;

    public List<ServiceCall> saveAll(List<ServiceCall> serviceCalls) {
        return serviceCalls.stream().map(this::save).collect(Collectors.toList());
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
        String address = serviceCall.getAddress().replace("XX", "00");
        Position position = geocodingService.geocodeAddressToGPS(address + " St. Louis MO");
        if (position != null) {
            serviceCall.setLongitude(position.getLongitude());
            serviceCall.setLatitude(position.getLatitude());
        } else {
            log.info("No GPS location found for address %s".formatted(address));
        }
    }

    public Page<ServiceCall> findAllWithCallTimeAfter(TimeSinceField timeSinceField, Pageable pageable) {
        LocalDateTime dateTimeAgo = switch (timeSinceField) {
            case FIFTEEN_MIN -> LocalDateTime.now().minusMinutes(15);
            case TWO_HOURS -> LocalDateTime.now().minusHours(2);
            case ANY -> null;
        };
        return dateTimeAgo == null ? repository.findAll(pageable) : repository.findByCallTimeLessThan(dateTimeAgo, pageable);
    }

    public void deleteAll() {
        if (repository.count() > 9_500) {
            repository.deleteAll();
        }
    }
}