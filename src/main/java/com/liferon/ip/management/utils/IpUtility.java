package com.liferon.ip.management.utils;

public class IpUtility {
    public static int ipAddressToInteger(String ipAddress) {
        String[] ipBytes = ipAddress.split("\\.");

        int ipAddressInt = Integer.parseInt(ipBytes[0]);
        ipAddressInt = (ipAddressInt << 8) | Integer.parseInt(ipBytes[1]);
        ipAddressInt = (ipAddressInt << 8) | Integer.parseInt(ipBytes[2]);
        ipAddressInt = (ipAddressInt << 8) | Integer.parseInt(ipBytes[3]);

        return ipAddressInt;
    }
}
