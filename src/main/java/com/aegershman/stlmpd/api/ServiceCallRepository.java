package com.aegershman.stlmpd.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ServiceCallRepository extends JpaRepository<ServiceCall, Long> {

    ServiceCall findByServiceCallId(String serviceCallId);

    Page<ServiceCall> findByCallTimeLessThan(LocalDateTime dateAfter, Pageable pageable);
}
