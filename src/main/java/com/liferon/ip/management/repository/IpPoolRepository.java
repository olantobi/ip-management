package com.liferon.ip.management.repository;

import com.liferon.ip.management.model.IpPool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpPoolRepository extends JpaRepository<IpPool, Integer> {

    @Query("SELECT SUM(p.totalCapacity) FROM IpPool p")
    long getGrandTotalCapacity();

    List<IpPool> findAllByOrderByUsedCapacityDesc();

    @Query("SELECT p FROM IpPool p WHERE p.usedCapacity < p.totalCapacity ORDER BY p.usedCapacity DESC")
    List<IpPool> findAllAvailableOrderByUsedCapacityDesc();
}
