package com.liferon.ip.management.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("Blacklist IP address Request")
public class BlacklistIpRequestDto {
    @ApiModelProperty(value = "Ip Address", required = true)
    @NotEmpty(message = "IP Address is required")
    @Pattern(regexp = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", flags = Pattern.Flag.UNICODE_CASE, message = "Invalid ip address")
    private String ipAddress;

    @NotNull(message = "IP poolId is required")
    private int poolId;
}
