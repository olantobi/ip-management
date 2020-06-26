package com.liferon.ip.management.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("Reserve dynamic IP address Request")
public class DynamicIpRequestDto {
    @NotNull(message = "Quantity is is required")
    private int quantity;
}
