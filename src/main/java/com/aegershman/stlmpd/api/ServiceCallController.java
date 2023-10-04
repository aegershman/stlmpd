package com.aegershman.stlmpd.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ServiceCallController {

    private final ServiceCallService serviceCallService;

    @GetMapping({"/", "/map"})
    public String map(Model model,
                      @RequestParam(defaultValue = "1") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(defaultValue = "CALL_TIME") SortField sortField,
                      @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
                      @RequestParam(defaultValue = "ANY") TimeSinceField timeSince
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, sortDirection, sortField.getDatabaseFieldName());

        Page<ServiceCall> serviceCallPage = serviceCallService.findAllWithCallTimeAfter(timeSince, pageable);
        List<ServiceCall> serviceCalls = serviceCallPage.getContent();

        model.addAttribute("serviceCalls", serviceCalls);
        model.addAttribute("currentPage", serviceCallPage.getNumber() + 1);
        model.addAttribute("totalPages", serviceCallPage.getTotalPages());
        model.addAttribute("totalCalls", serviceCallPage.getTotalElements());
        model.addAttribute("pageSize", size);

        int totalPages = serviceCallPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "map";
    }

    @GetMapping("/stats")
    public String stats(Model model,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "CALL_TIME") SortField sortField,
                        @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection,
                        @RequestParam(defaultValue = "ANY") TimeSinceField timeSince
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, sortDirection, sortField.getDatabaseFieldName());

        Page<ServiceCall> serviceCallPage = serviceCallService.findAllWithCallTimeAfter(timeSince, pageable);
        List<ServiceCall> serviceCalls = serviceCallPage.getContent();

        model.addAttribute("serviceCalls", serviceCalls);
        model.addAttribute("currentPage", serviceCallPage.getNumber() + 1);
        model.addAttribute("totalPages", serviceCallPage.getTotalPages());
        model.addAttribute("totalCalls", serviceCallPage.getTotalElements());
        model.addAttribute("pageSize", size);

        int totalPages = serviceCallPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "stats";
    }
}
