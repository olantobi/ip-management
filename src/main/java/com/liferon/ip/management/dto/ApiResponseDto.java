package com.liferon.ip.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("API Response")
public class ApiResponseDto<T> implements Serializable {

    @ApiModelProperty(value = "The response main content.")
    private T data;

    @ApiModelProperty(value = "The error or success message.")
    private String message;

    @ApiModelProperty(value = "The success flag.")
    private boolean successful;
}

