package com.liferon.ip.management.model;

import com.liferon.ip.management.utils.IpUtility;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ip_pool")
public class IpPool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "total_capacity", nullable = false)
    private int totalCapacity;

    @Column(name = "used_capacity", nullable = false)
    private int usedCapacity;

    @Column(name = "lower_bound", nullable = false)
    private String lowerBound;

    @Column(name = "upper_bound", nullable = false)
    private String upperBound;
}
