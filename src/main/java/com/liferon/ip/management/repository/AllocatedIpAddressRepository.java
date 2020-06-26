package com.liferon.ip.management.repository;

import com.liferon.ip.management.model.AllocatedIpAddress;
import com.liferon.ip.management.model.ResourceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllocatedIpAddressRepository extends JpaRepository<AllocatedIpAddress, Long> {

    Optional<AllocatedIpAddress> findByValue(String ipAddress);
    Optional<AllocatedIpAddress> findByValueAndResourceStateNot(String ipAddress, ResourceState state);
    Optional<AllocatedIpAddress> findByValueAndResourceState(String ipAddress, ResourceState state);
}
