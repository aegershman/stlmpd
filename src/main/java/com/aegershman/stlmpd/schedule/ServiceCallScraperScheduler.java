package com.aegershman.stlmpd.schedule;

import com.aegershman.stlmpd.api.ServiceCallService;
import com.aegershman.stlmpd.scrape.ServiceCallScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class ServiceCallScraperScheduler {

    private final ServiceCallScraper serviceCallScraper;
    private final ServiceCallService serviceCallService;

    @Scheduled(fixedRateString = "${stlmpd.poll-interval-millis}")
    public void run() throws IOException {
        var calls = serviceCallScraper.scrape();
        var saved = serviceCallService.saveAll(calls);
        saved.forEach(call -> log.info(String.valueOf(call)));
    }
}
