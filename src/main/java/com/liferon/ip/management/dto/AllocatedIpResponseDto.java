package com.liferon.ip.management.dto;

import lombok.Data;

@Data
public class AllocatedIpResponseDto {
    private String value;
    private String resourceState;
    private int id;
    private int ipPoolId;
}
