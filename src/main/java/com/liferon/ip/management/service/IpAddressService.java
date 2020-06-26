package com.liferon.ip.management.service;

import com.liferon.ip.management.dto.*;

import java.util.List;

public interface IpAddressService {
    List<AllocatedIpResponseDto> reserveDynamicIpAddress(DynamicIpRequestDto ipRequestDto);
    AllocatedIpResponseDto reserveStaticIpAddress(StaticIpRequestDto ipRequestDto);
    void blacklistIpAddress(BlacklistIpRequestDto ipRequestDto);
    void freeIpAddress(FreeIpRequestDto ipRequestDto);
    AllocatedIpResponseDto retrieveIpResource(String ipAddress);
}
