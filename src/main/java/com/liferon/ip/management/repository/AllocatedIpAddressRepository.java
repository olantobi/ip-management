package com.liferon.ip.management.repository;

import com.liferon.ip.management.model.AllocatedIpAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocatedIpAddressRepository extends JpaRepository<AllocatedIpAddress, Long> {
}
