package com.liferon.ip.management.repository;

import com.liferon.ip.management.model.IpPool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpPoolRepository extends JpaRepository<IpPool, Integer> {
}
