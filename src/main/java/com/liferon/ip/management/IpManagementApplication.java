package com.liferon.ip.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IpManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpManagementApplication.class, args);
    }

}
