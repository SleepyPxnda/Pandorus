package de.cloudypanda.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class NetworkHelper {

    public static NetworkStatus pingIp(String ipAddress) {

        List<String> ipParts = Arrays.stream(ipAddress.split(":")).toList();
        String host = ipParts.get(0);
        int port = 80;

        if(ipParts.size() > 1){
            port = tryParsePort(ipParts.get(1));
        }

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000);
            return NetworkStatus.REACHABLE;
        } catch (IOException e) {
            return NetworkStatus.UNREACHABLE; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    private static int tryParsePort(String port) {
        try{
            return Integer.parseInt(port);
        }catch (NumberFormatException e){
            return 80;
        }
    }
}
