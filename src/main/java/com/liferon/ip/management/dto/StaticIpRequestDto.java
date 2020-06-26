package com.liferon.ip.management.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaticIpRequestDto {

    @ApiModelProperty(value = "Ip Address", required = true)
    @NotEmpty(message = "IP Address is required")
    private String ipAddress;

    @ApiModelProperty(value = "Ip pool id", required = true)
    @NotNull(message = "IP pool Id is required")
    private int poolId;
}
