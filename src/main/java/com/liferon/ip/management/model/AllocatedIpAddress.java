package com.liferon.ip.management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "allocated_ip_address")
public class AllocatedIpAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ip_pool_id")
    private IpPool ipPool;

    @Column(name = "value", unique = true, nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    private ResourceState resourceState;
}
