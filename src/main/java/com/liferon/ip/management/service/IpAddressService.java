package com.liferon.ip.management.service;

import com.liferon.ip.management.dto.*;
import com.liferon.ip.management.exception.InvalidRequestParameterException;

import java.util.List;

public interface IpAddressService {
    List<AllocatedIpResponseDto> reserveDynamicIpAddress(DynamicIpRequestDto ipRequestDto);
    AllocatedIpResponseDto reserveStaticIpAddress(StaticIpRequestDto ipRequestDto);
    AllocatedIpResponseDto blacklistIpAddress(BlacklistIpRequestDto ipRequestDto);
    AllocatedIpResponseDto freeIpAddress(FreeIpRequestDto ipRequestDto) throws InvalidRequestParameterException;
    AllocatedIpResponseDto retrieveIpResource(String ipAddress);
}
