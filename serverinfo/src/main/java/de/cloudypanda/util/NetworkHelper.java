package de.cloudypanda.util;

import java.net.InetAddress;

public class NetworkHelper {

    public static String pingIp(String ipAddress) {
        InetAddress inet;

        try {
            inet = InetAddress.getByName(ipAddress);
            return inet.isReachable(500) ? "YES" : "NO";
        } catch (Exception e) {
            return "N/A";
        }
    }
}
