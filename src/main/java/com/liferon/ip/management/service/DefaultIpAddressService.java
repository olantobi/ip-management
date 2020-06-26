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
    private static final String ipRegex = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

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

    @Override
    public AllocatedIpResponseDto reserveStaticIpAddress(StaticIpRequestDto ipRequestDto) {
        if (!validateIpAddress(ipRequestDto.getIpAddress())) {
            throw new InvalidRequestParameterException("Invalid IP address");
        }

        Optional<IpPool> ipPoolOption = ipPoolRepository.findById(ipRequestDto.getPoolId());
        if (!ipPoolOption.isPresent()) {
            throw new InvalidRequestParameterException("Invalid IP pool id");
        }

        IpPool ipPool = ipPoolOption.get();
        if (ipPool.isFilledUp()) {
            throw new InvalidRequestParameterException("Requested IP pool is filled up");
        }

        if (IpUtility.ipIsWithinRange(ipPool.getLowerBound(), ipPool.getUpperBound(), ipRequestDto.getIpAddress())) {
            throw new InvalidRequestParameterException("Requested ip address is not within specified ip pool range");
        }

        Optional<AllocatedIpAddress> allocatedIpOption = ipAddressRepository.findByValue(ipRequestDto.getIpAddress());
        if (allocatedIpOption.isPresent() && allocatedIpOption.get().getResourceState() == ResourceState.BLACKLISTED) {
            throw new InvalidRequestParameterException("Requested ip address is blacklisted");
        }

        if (allocatedIpOption.isPresent() && allocatedIpOption.get().getResourceState() == ResourceState.RESERVED) {
            throw new InvalidRequestParameterException("Requested ip address is not available");
        }

        AllocatedIpAddress ipAddress = (allocatedIpOption.isPresent() && allocatedIpOption.get().getResourceState() == ResourceState.FREE) ?
        allocatedIpOption.get() : AllocatedIpAddress.builder().
                ipPool(ipPool)
                .value(ipRequestDto.getIpAddress())
                .build();
        ipAddress.setResourceState(ResourceState.RESERVED);

        AllocatedIpAddress allocatedIp = ipAddressRepository.save(ipAddress);

        ipPool.incrementUsedCapacity();
        ipPoolRepository.save(ipPool);

        return OrikaUtils.map(allocatedIp, AllocatedIpResponseDto.class);
    }

    @Override
    public AllocatedIpResponseDto blacklistIpAddress(BlacklistIpRequestDto ipRequestDto) {
        if (!validateIpAddress(ipRequestDto.getIpAddress())) {
            throw new InvalidRequestParameterException("Invalid IP address");
        }

        Optional<IpPool> ipPoolOption = ipPoolRepository.findById(ipRequestDto.getPoolId());
        if (!ipPoolOption.isPresent()) {
            throw new InvalidRequestParameterException("Invalid ip pool id");
        }

        if (ipAddressRepository.findByValueAndResourceState(ipRequestDto.getIpAddress(), ResourceState.RESERVED).isPresent()) {
            throw new InvalidRequestParameterException("Requested ip address is already reserved");
        }
        IpPool ipPool = ipPoolOption.get();

        if (IpUtility.ipIsWithinRange(ipPool.getLowerBound(), ipPool.getUpperBound(), ipRequestDto.getIpAddress())) {
            throw new InvalidRequestParameterException("Requested ip address is not within specified ip pool range");
        }

        AllocatedIpAddress ipAddress = AllocatedIpAddress.builder().
                ipPool(ipPool)
                .value(ipRequestDto.getIpAddress())
                .resourceState(ResourceState.BLACKLISTED)
                .build();
        AllocatedIpAddress allocatedIp = ipAddressRepository.save(ipAddress);

        ipPool.incrementUsedCapacity();
        ipPoolRepository.save(ipPool);

        return OrikaUtils.map(allocatedIp, AllocatedIpResponseDto.class);
    }

    @Override
    public AllocatedIpResponseDto freeIpAddress(FreeIpRequestDto ipRequestDto) throws InvalidRequestParameterException {
        Optional<IpPool> ipPoolOption = ipPoolRepository.findById(ipRequestDto.getPoolId());
        if (!ipPoolOption.isPresent()) {
            throw new InvalidRequestParameterException("Invalid ip pool id");
        }

        IpPool ipPool = ipPoolOption.get();
        if (IpUtility.ipIsWithinRange(ipPool.getLowerBound(), ipPool.getUpperBound(), ipRequestDto.getIpAddress())) {
            throw new InvalidRequestParameterException("Requested IP address is not within specified ip pool range");
        }

        Optional<AllocatedIpAddress> allocatedIpAddressOption = ipAddressRepository.findByValue(ipRequestDto.getIpAddress());
        if (!allocatedIpAddressOption.isPresent()) {
            throw new InvalidRequestParameterException("Requested IP address not allocated");
        }

        AllocatedIpAddress ipAddress = allocatedIpAddressOption.get();
        ipAddress.setResourceState(ResourceState.FREE);
        AllocatedIpAddress allocatedIp = ipAddressRepository.save(ipAddress);

        ipPool.decrementUsedCapacity();
        ipPoolRepository.save(ipPool);

        return OrikaUtils.map(allocatedIp, AllocatedIpResponseDto.class);
    }

    @Override
    public AllocatedIpResponseDto retrieveIpResource(String ipAddress) {
        Optional<AllocatedIpAddress> ipAddressOption = ipAddressRepository.findByValue(ipAddress);

        if (!ipAddressOption.isPresent()) {
            throw new InvalidRequestParameterException("This ip address is not allocated");
        }

        return OrikaUtils.map(ipAddressOption.get(), AllocatedIpResponseDto.class);
    }

    private List<AllocatedIpAddress> generateIpAddressRange(int startIp, int qty, IpPool ipPool) {
        List<AllocatedIpAddress> ipAddresses = new ArrayList<>();
        for (int ip = 0; ip < qty; ip++) {
            String ipAddress = IpUtility.integerToIpAddress(startIp + ip);

            if (!ipAddressRepository.findByValueAndResourceStateNot(ipAddress, ResourceState.FREE).isPresent()) {
                AllocatedIpAddress allocatedIpAddress = AllocatedIpAddress.builder()
                        .ipPool(ipPool)
                        .value(ipAddress)
                        .resourceState(ResourceState.RESERVED).build();
                ipAddresses.add(allocatedIpAddress);
            }
        }

        return ipAddresses;
    }

    private boolean validateIpAddress(String ipAddress) {
        return ipAddress.matches(ipRegex);
    }
}
