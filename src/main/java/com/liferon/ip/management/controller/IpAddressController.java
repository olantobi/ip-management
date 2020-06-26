package com.liferon.ip.management.controller;

import com.liferon.ip.management.dto.*;
import com.liferon.ip.management.repository.IpPoolRepository;
import com.liferon.ip.management.service.IpAddressService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ip-address")
public class IpAddressController {
    private final IpPoolRepository ipPoolRepository;
    private final IpAddressService ipAddressService;

    @ApiOperation("Reserves a dynamic IP address.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns the reserved ip addresses", response = ApiResponseDto.class),
            @ApiResponse(code = 400, message = "<li>Error if any of the mandatory fields were not filled.</li>" +
                    "<li>Error if the demand is greater than available resources.</li>", response = ApiResponseDto.class)
    })
    @PostMapping("/reserve/dynamic")
    public ApiResponseDto<?> reserveDynamicAddress(final @Valid @RequestBody DynamicIpRequestDto ipRequestDto) {
        List<AllocatedIpResponseDto> ipAddresses = ipAddressService.reserveDynamicIpAddress(ipRequestDto);

        return ApiResponseDto.<List<AllocatedIpResponseDto>>builder()
                .data(ipAddresses)
                .successful(true)
                .message("Successful")
                .build();
    }

    @ApiOperation("Reserves a static IP address.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns the reserved ip address", response = ApiResponseDto.class),
            @ApiResponse(code = 400, message = "<li>Error if any of the mandatory fields were not filled.</li>" +
                    "<li>Error if the required ip address is not within the IP pool range.</li>", response = ApiResponseDto.class)
    })
    @PostMapping("/reserve/static")
    public ApiResponseDto<?> reserveStaticAddress(final @Valid @RequestBody StaticIpRequestDto ipRequestDto) {
        AllocatedIpResponseDto allocatedIpAddress = ipAddressService.reserveStaticIpAddress(ipRequestDto);

        return ApiResponseDto.<AllocatedIpResponseDto>builder()
                .data(allocatedIpAddress)
                .successful(true)
                .message("Successful")
                .build();
    }

    @ApiOperation("Blacklists an IP address.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns the reserved ip address", response = ApiResponseDto.class),
            @ApiResponse(code = 400, message = "<li>Error if any of the mandatory fields were not filled.</li>" +
                    "<li>Error if the required ip address is not within the IP pool range.</li>", response = ApiResponseDto.class)
    })
    @PostMapping("/blacklist")
    public ApiResponseDto<?> blacklistAddress(final @Valid @RequestBody BlacklistIpRequestDto ipRequestDto) {
        ipAddressService.blacklistIpAddress(ipRequestDto);

        return ApiResponseDto.builder()
                .successful(true)
                .message("Successful")
                .build();
    }

    @ApiOperation("Frees an IP address.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns the reserved ip address", response = ApiResponseDto.class),
            @ApiResponse(code = 400, message = "<li>Error if any of the mandatory fields were not filled.</li>" +
                    "<li>Error if the required ip address is not within the IP pool range.</li>", response = ApiResponseDto.class)
    })
    @PostMapping("/free")
    public ApiResponseDto<?> freeAddress(final @Valid @RequestBody FreeIpRequestDto requestDto) {
        ipAddressService.freeIpAddress(requestDto);

        return ApiResponseDto.builder()
                .successful(true)
                .message("Successful")
                .build();
    }

    @ApiOperation("Retrieves an IP resource.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Returns the reserved ip address", response = ApiResponseDto.class),
            @ApiResponse(code = 404, message = "<li>Error if the ip address is not found.</li>", response = ApiResponseDto.class)
    })
    @GetMapping("/{ipAddress}/retrieve")
    public ApiResponseDto<?> retrieveAddress(@PathVariable("ipAddress") String ipAddress) {
        AllocatedIpResponseDto allocatedIpAddress = ipAddressService.retrieveIpResource(ipAddress);

        return ApiResponseDto.<AllocatedIpResponseDto>builder()
                .data(allocatedIpAddress)
                .successful(true)
                .message("Successful")
                .build();
    }
}
