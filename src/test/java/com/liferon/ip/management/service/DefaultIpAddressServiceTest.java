package com.liferon.ip.management.service;


import com.liferon.ip.management.dto.AllocatedIpResponseDto;
import com.liferon.ip.management.dto.DynamicIpRequestDto;
import com.liferon.ip.management.dto.IpPoolDto;
import com.liferon.ip.management.dto.StaticIpRequestDto;
import com.liferon.ip.management.exception.InvalidRequestParameterException;
import com.liferon.ip.management.model.AllocatedIpAddress;
import com.liferon.ip.management.model.IpPool;
import com.liferon.ip.management.model.ResourceState;
import com.liferon.ip.management.repository.AllocatedIpAddressRepository;
import com.liferon.ip.management.repository.IpPoolRepository;
import com.liferon.ip.management.utils.JSONHelper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultIpAddressServiceTest {
    @Mock
    private AllocatedIpAddressRepository ipAddressRepository;
    @Mock
    private IpPoolRepository ipPoolRepository;
    @InjectMocks
    private DefaultIpAddressService ipAddressService;
    private MapperFacade mapperFacade;

    @Before
    public void init() {
        mapperFacade = new DefaultMapperFactory.Builder().build().getMapperFacade();
    }

    @Test
    public void reserveDynamicIpAddress() throws IOException {
        int qty = 5;

        List<IpPoolDto> ipPoolDtos = JSONHelper.fileToBeanList("/ip-pools.json", IpPoolDto.class);
        List<IpPool> ipPools = mapperFacade.mapAsList(ipPoolDtos, IpPool.class);

        when(ipPoolRepository.getGrandTotalCapacity()).thenReturn(20L);
        when(ipPoolRepository.findAllAvailableOrderByUsedCapacityDesc()).thenReturn(ipPools);
        DynamicIpRequestDto ipRequestDto = new DynamicIpRequestDto(qty);
        List<AllocatedIpResponseDto> ipAddresses = ipAddressService.reserveDynamicIpAddress(ipRequestDto);

        Assert.assertEquals(qty, ipAddresses.size());
        Assert.assertEquals("10.70.26.1", ipAddresses.get(0).getValue());
        Assert.assertEquals("10.70.26.2", ipAddresses.get(1).getValue());
        Assert.assertEquals("10.70.26.3", ipAddresses.get(2).getValue());
        Assert.assertEquals("10.70.26.4", ipAddresses.get(3).getValue());
        Assert.assertEquals("10.70.26.5", ipAddresses.get(4).getValue());
    }

    @Test(expected = InvalidRequestParameterException.class)
    public void reserveDynamicIpAboveAvailableResourcesShouldThrowException() throws IOException {
        int qty = 25;

        when(ipPoolRepository.getGrandTotalCapacity()).thenReturn(20L);
        DynamicIpRequestDto ipRequestDto = new DynamicIpRequestDto(qty);
        ipAddressService.reserveDynamicIpAddress(ipRequestDto);
    }

    @Test
    public void reserveStaticIpAddress() {
        String requestedIp = "10.70.26.54";
        int poolId = 1;
        IpPool ipPool = IpPool.builder()
                .id(poolId)
                .totalCapacity(200)
                .usedCapacity(50)
                .lowerBound("10.70.25.1")
                .upperBound("10.70.25.100")
                .build();


        when(ipPoolRepository.findById(poolId)).thenReturn(Optional.of(ipPool));
        when(ipAddressRepository.save(any(AllocatedIpAddress.class)))
                .thenReturn(AllocatedIpAddress.builder()
                .ipPool(ipPool)
                .value(requestedIp)
                .build());

        StaticIpRequestDto ipRequestDto = new StaticIpRequestDto(requestedIp, poolId);
        AllocatedIpResponseDto ipAddress = ipAddressService.reserveStaticIpAddress(ipRequestDto);

        Assert.assertNotNull(ipAddress);
        Assert.assertEquals(requestedIp, ipAddress.getValue());
    }

    @Test(expected = InvalidRequestParameterException.class)
    public void reserveStaticIpAddressWithInvalidPoolIdShouldThrowException() {
        String requestedIp = "10.70.26.54";
        int poolId = 1;

        StaticIpRequestDto ipRequestDto = new StaticIpRequestDto(requestedIp, poolId);
        ipAddressService.reserveStaticIpAddress(ipRequestDto);
    }

    @Test(expected = InvalidRequestParameterException.class)
    public void reserveStaticIpAddressWithFilledPoolIdShouldThrowException() {
        String requestedIp = "10.70.26.54";
        int poolId = 1;
        IpPool ipPool = IpPool.builder()
                .id(poolId)
                .totalCapacity(200)
                .usedCapacity(200)
                .lowerBound("10.70.25.1")
                .upperBound("10.70.25.100")
                .build();

        when(ipPoolRepository.findById(poolId)).thenReturn(Optional.of(ipPool));

        StaticIpRequestDto ipRequestDto = new StaticIpRequestDto(requestedIp, poolId);
        ipAddressService.reserveStaticIpAddress(ipRequestDto);
    }

    @Test(expected = InvalidRequestParameterException.class)
    public void reserveStaticIpAddressWithAllocatedIpAddressShouldThrowException() {
        String requestedIp = "10.70.26.54";
        int poolId = 1;
        IpPool ipPool = IpPool.builder()
                .id(poolId)
                .totalCapacity(200)
                .usedCapacity(50)
                .lowerBound("10.70.25.1")
                .upperBound("10.70.25.100")
                .build();

        when(ipPoolRepository.findById(poolId)).thenReturn(Optional.of(ipPool));
        when(ipAddressRepository.findByValue(requestedIp))
                .thenReturn(Optional.of(AllocatedIpAddress.builder()
                .ipPool(ipPool)
                .resourceState(ResourceState.RESERVED)
                .value(requestedIp)
                .build()));

        StaticIpRequestDto ipRequestDto = new StaticIpRequestDto(requestedIp, poolId);
        ipAddressService.reserveStaticIpAddress(ipRequestDto);
    }

    @org.junit.Test
    public void blacklistIpAddress() {
    }

    @org.junit.Test
    public void freeIpAddress() {
    }

    @org.junit.Test
    public void retrieveIpResource() {
    }
}