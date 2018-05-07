package com.myzyb.appNYB.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by xialv on 2018/2/2.
 */

public class DeviceUtil {
    public static void getIp(Context context) {
        ip(context);
    }
    public static String ip(Context ctx){
        String ip = "1.1.1.1";
        ConnectivityManager conMann = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobileNetworkInfo.isConnected()) {
            ip = getLocalIpAddress();
            System.out.println("本地ip-----"+ip);
        }else if(wifiNetworkInfo.isConnected())
        {

            WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
            System.out.println("wifi_ip地址为------"+ip);
        }
        return ip;
    }
    public static String getLocalIpAddress() {
        try {
            String ipv4;
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni: nilist)
            {
                ArrayList<InetAddress>  ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address: ialist){
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address)
                    {
                        return address.getHostAddress();
                    }
                }

            }

        } catch (SocketException ex) {

        }
        return null;
    }
    public static String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

}
