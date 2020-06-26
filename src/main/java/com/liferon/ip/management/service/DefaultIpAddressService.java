package com.liferon.ip.management.service;

import com.liferon.ip.management.dto.*;
import com.liferon.ip.management.exception.InvalidRequestParameterException;
import com.liferon.ip.management.model.AllocatedIpAddress;
import com.liferon.ip.management.model.IpPool;
import com.liferon.ip.management.model.ResourceState;
import com.liferon.ip.management.repository.AllocatedIpAddressRepository;
import com.liferon.ip.management.repository.IpPoolRepository;
import com.liferon.ip.management.utils.IpUtility;
import com.liferon.ip.management.utils.OrikaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultIpAddressService implements IpAddressService {
    private final AllocatedIpAddressRepository ipAddressRepository;
    private final IpPoolRepository ipPoolRepository;

    @Override
    public List<AllocatedIpResponseDto> reserveDynamicIpAddress(DynamicIpRequestDto ipRequestDto) {
        int qty = ipRequestDto.getQuantity();
        long grandTotalCapacity = ipPoolRepository.getGrandTotalCapacity();

        if (qty > grandTotalCapacity) {
            throw new InvalidRequestParameterException("Quantity requested is more than available resources");
        }

        List<IpPool> ipPoolList = ipPoolRepository.findAllAvailableOrderByUsedCapacityDesc();
        List<AllocatedIpAddress> allocatedIps = new ArrayList<>();

        for (IpPool ipPool : ipPoolList) {
            String lowerBound = ipPool.getLowerBound();
            int lowerBoundInt = IpUtility.ipAddressToInteger(lowerBound) + ipPool.getUsedCapacity();
            int capacity = ipPool.getTotalCapacity() - ipPool.getUsedCapacity();

            int qtyToGenerateInPool = Math.min(qty, capacity);
            List<AllocatedIpAddress> generatedIps = generateIpAddressRange(lowerBoundInt, qtyToGenerateInPool, ipPool);

            allocatedIps.addAll(generatedIps);
            ipPool.setUsedCapacity(ipPool.getUsedCapacity() + qtyToGenerateInPool);
            ipPoolRepository.save(ipPool);

            if (qty < capacity) {
                break;
            }
        }

        ipAddressRepository.saveAll(allocatedIps);
        return OrikaUtils.map(allocatedIps, AllocatedIpResponseDto.class);
    }

    private List<AllocatedIpAddress> generateIpAddressRange(int startIp, int qty, IpPool ipPool) {
        List<AllocatedIpAddress> ipAddresses = new ArrayList<>();
        for (int ip = 0; ip < qty; ip++) {
            String ipAddress = IpUtility.integerToIpAddress(startIp + ip);

            if (!ipAddressRepository.findByValue(ipAddress).isPresent()) {
                AllocatedIpAddress allocatedIpAddress = AllocatedIpAddress.builder()
                        .ipPool(ipPool)
                        .value(ipAddress)
                        .resourceState(ResourceState.RESERVED).build();
                ipAddresses.add(allocatedIpAddress);
            }
        }

        return ipAddresses;
    }

    @Override
    public AllocatedIpResponseDto reserveStaticIpAddress(StaticIpRequestDto ipRequestDto) {
        Optional<IpPool> ipPoolOption = ipPoolRepository.findById(ipRequestDto.getPoolId());
        if (!ipPoolOption.isPresent()) {
            throw new InvalidRequestParameterException("Invalid ip pool id");
        }

        IpPool ipPool = ipPoolOption.get();
        if (ipPool.isFilledUp()) {
            throw new InvalidRequestParameterException("Requested ip pool is filled up");
        }

        if (ipAddressRepository.findByValue(ipRequestDto.getIpAddress()).isPresent()) {
            throw new InvalidRequestParameterException("Requested ip address is not available");
        }

        ipPool.incrementUsedCapacity();
        ipPoolRepository.save(ipPool);

        AllocatedIpAddress ipAddress = AllocatedIpAddress.builder().
                ipPool(ipPool)
                .value(ipRequestDto.getIpAddress())
                .resourceState(ResourceState.RESERVED)
                .build();

        AllocatedIpAddress allocatedIp = ipAddressRepository.save(ipAddress);
        return OrikaUtils.map(allocatedIp, AllocatedIpResponseDto.class);
    }

    @Override
    public void blacklistIpAddress(BlacklistIpRequestDto ipRequestDto) {

    }

    @Override
    public void freeIpAddress(FreeIpRequestDto ipRequestDto) {

    }

    @Override
    public AllocatedIpResponseDto retrieveIpResource(String ipAddress) {
        Optional<AllocatedIpAddress> ipAddressOption = ipAddressRepository.findByValue(ipAddress);

        if (!ipAddressOption.isPresent()) {
            throw new InvalidRequestParameterException("This ip address is not allocated");
        }

        return OrikaUtils.map(ipAddressOption.get(), AllocatedIpResponseDto.class);
    }
}
