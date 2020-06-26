package com.liferon.ip.management.repository;

import com.liferon.ip.management.model.AllocatedIpAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllocatedIpAddressRepository extends JpaRepository<AllocatedIpAddress, Long> {

    Optional<AllocatedIpAddress> findByValue(String ipAddress);
}
