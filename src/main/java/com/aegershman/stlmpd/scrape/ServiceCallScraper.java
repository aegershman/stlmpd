package com.aegershman.stlmpd.scrape;

import com.aegershman.stlmpd.StlmpdProperties;
import com.aegershman.stlmpd.api.ServiceCall;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceCallScraper {

    private final StlmpdProperties stlmpdProperties;

    public List<ServiceCall> extract() throws IOException {
        Document doc = Jsoup.connect(stlmpdProperties.getApiUrl()).get();
        Element body = doc.body();
        Element tParent = body.getElementById("gvData");
        Element tBody = tParent.selectFirst("tbody");
        Elements rows = tBody.children();
        List<ServiceCall> serviceCalls = new ArrayList<>();
        for (Element row : rows) {
            Elements columns = row.getElementsByTag("td");
            String timeStr = columns.get(0).text();
            LocalDateTime localDateTime = LocalDateTimeConverter.convert(timeStr);
            String serviceCallId = columns.get(1).text();
            String address = columns.get(2).text();
            String reason = columns.get(3).text();
            ServiceCall call = new ServiceCall();
            call.setCallTime(localDateTime);
            call.setServiceCallId(serviceCallId);
            call.setAddress(address);
            call.setReason(reason);
            serviceCalls.add(call);
        }
        return serviceCalls;
    }

}
