package Server;

import Watch.WatchHmManager;
import Watch.WatchHmsManager;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLOutput;

public class WatchServer {
    int port = 3124;
    InetAddress host;
    WatchHmsManager watch = new WatchHmsManager(12,0,0);

    public WatchServer() {
        try {
            host = InetAddress.getLocalHost();
            ServerSocket ss = new ServerSocket(3124, 0, host);
            System.out.println("Server started");

            while(true) {
                Socket cs = ss.accept();
                System.out.println("Client connected");
                WCS wcs = new WCS(cs, watch);
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new WatchServer();
    }
}
