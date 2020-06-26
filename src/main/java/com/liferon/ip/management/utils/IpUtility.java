package com.liferon.ip.management.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.net.InetAddress;

@Slf4j
public class IpUtility {
    public static int ipAddressToInteger(String ipAddress) {
        String[] ipBytes = ipAddress.split("\\.");

        int ipAddressInt = Integer.parseInt(ipBytes[0]);
        ipAddressInt = (ipAddressInt << 8) | Integer.parseInt(ipBytes[1]);
        ipAddressInt = (ipAddressInt << 8) | Integer.parseInt(ipBytes[2]);
        ipAddressInt = (ipAddressInt << 8) | Integer.parseInt(ipBytes[3]);

        return ipAddressInt;
    }

    public static String integerToIpAddress(int intValue) {
        String ipAddress = "";
        try {

            byte[] bytes = BigInteger.valueOf(intValue).toByteArray();
            InetAddress address = InetAddress.getByAddress(bytes);
            ipAddress = address.getHostAddress();
        } catch (Exception ex) {
            log.error("Error while generating ip address", ex);
        }
        return ipAddress;
    }

    public static boolean ipIsWithinRange(String lowerBound, String upperBound, String ipAddress) {
        int lowerBoundInt = ipAddressToInteger(lowerBound);
        int upperBoundInt = ipAddressToInteger(upperBound);
        int ipAddressInt = ipAddressToInteger(ipAddress);

        return (ipAddressInt >= lowerBoundInt && ipAddressInt <= upperBoundInt);
    }
}
