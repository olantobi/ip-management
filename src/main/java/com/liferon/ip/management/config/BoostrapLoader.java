package com.liferon.ip.management.config;

import com.liferon.ip.management.dto.IpPoolDto;
import com.liferon.ip.management.model.IpPool;
import com.liferon.ip.management.repository.IpPoolRepository;
import com.liferon.ip.management.utils.JSONHelper;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BoostrapLoader implements CommandLineRunner {
    private final IpPoolRepository ipPoolRepository;
    private final MapperFacade mapperFacade;

    @Value("classpath:ip-pools.json")
    private Resource res;

    @Override
    public void run(String... args) throws Exception {
       List<IpPoolDto> ipPoolDtoList = JSONHelper.fileToBeanList(res.getFile(), IpPoolDto.class);

       List<IpPool> ipPools = mapperFacade.mapAsList(ipPoolDtoList, IpPool.class);

       ipPoolRepository.saveAll(ipPools);
    }
}
