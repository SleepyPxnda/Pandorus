package de.cloudypanda.util;

import java.net.InetAddress;

public class NetworkHelper {

    public static NetworkStatus pingIp(String ipAddress) {
        InetAddress inet;

        try {
            inet = InetAddress.getByName(ipAddress);
            return inet.isReachable(500) ? NetworkStatus.REACHABLE : NetworkStatus.UNREACHABLE;
        } catch (Exception e) {
            return NetworkStatus.CANNOT_RESOLVE;
        }
    }
}
