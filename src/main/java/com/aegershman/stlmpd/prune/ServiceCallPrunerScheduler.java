package com.aegershman.stlmpd.prune;

import com.aegershman.stlmpd.api.ServiceCallService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceCallPrunerScheduler {
    private final ServiceCallService serviceCallService;

    @Scheduled(fixedRateString = "PT10H")
    public void run() {
        serviceCallService.deleteAll();
    }
}
