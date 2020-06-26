package com.liferon.ip.management.dto;

import com.liferon.ip.management.utils.IpUtility;
import lombok.Data;

@Data
public class IpPoolDto {
    private int id;
    private String description;
    private int usedCapacity;
    private int totalCapacity;
    private String lowerBound;
    private String upperBound;

    public int getTotalCapacity() {
        return IpUtility.ipAddressToInteger(this.upperBound)
                - IpUtility.ipAddressToInteger(this.lowerBound) + 1;
    }
}
