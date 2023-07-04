package com.dashingdanstrains.dcstester;




import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;


public class BonjourScanner {

    public String findTiuIpAddress() throws IOException, InterruptedException {
        try {
            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            BonjourListener listener = new BonjourListener();
            // Add a service listener
            jmdns.addServiceListener("_mth-dcs._tcp.local.", listener);
            // Wait a bit
            Thread.sleep(5000);
            var info =jmdns.list("_mth-dcs._tcp.local.");
            ServiceInfo serviceInfo = Arrays.stream(info).filter(x -> x.getName().contains("mth")).findFirst().get();
            System.out.println("Found TIU at "+serviceInfo.getInetAddresses()[0].getHostAddress()+serviceInfo.getPort());
            return serviceInfo.getInetAddresses()[0].getHostAddress()+":"+serviceInfo.getPort();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}

