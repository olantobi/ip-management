package com.liferon.ip.management.controller;

import com.liferon.ip.management.model.IpPool;
import com.liferon.ip.management.repository.IpPoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ip-address")
public class IpAddressController {
    private final IpPoolRepository ipPoolRepository;

    @PostMapping("/reserve/dynamic")
    public void reserveDynamicAddress() {

    }

    @PostMapping("/reserve/static")
    public void reserveStaticAddress() {

    }

    @PostMapping("/blacklist")
    public void blacklistAddress() {

    }

    @PostMapping("/free")
    public void freeAddress() {

    }

    @PostMapping("/retrieve")
    public void retrieveAddress() {

    }

    @GetMapping("/pools")
    public List<IpPool> getIpPools() {
        return ipPoolRepository.findAll();
    }
}
