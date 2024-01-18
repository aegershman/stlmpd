package com.aegershman.stlmpd.scrape;

import com.aegershman.stlmpd.StlmpdProperties;
import com.aegershman.stlmpd.api.ServiceCall;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServiceCallScraper {

    private final StlmpdProperties stlmpdProperties;

    public List<ServiceCall> scrape() throws IOException {
        return getDocument().body()
                .getElementById("gvData").selectFirst("tbody").children().stream()
                .map(row -> {
                    var columns = row.getElementsByTag("td");
                    ServiceCall call = new ServiceCall();
                    call.setCallTime(LocalDateTimeConverter.convert(columns.get(0).text()));
                    call.setServiceCallId(columns.get(1).text());
                    call.setAddress(columns.get(2).text());
                    call.setReason(columns.get(3).text());
                    return call;
                })
                .collect(Collectors.toList());
    }

    protected Document getDocument() throws IOException {
        return Jsoup.connect(stlmpdProperties.getApiUrl()).get();
    }
}
