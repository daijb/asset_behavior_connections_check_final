package com.lanysec.utils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author daijb
 * @date 2021/3/8 10:47
 */
public class SystemUtil {

    /**
     * 数据库用户名
     */
    public static String getMysqlUser() {
        return "root";
    }

    /**
     * 数据库密码
     */
    public static String getMysqlPassword() {
        return "Admin@123";
    }

    private static final String JDBC_URL = getJdbcUrl0();

    public static String getJdbcUrl() {
        return JDBC_URL;
    }

    private static String getJdbcUrl0() {
        return "jdbc:mysql://" + getHostIp0() + ":3306/csp?useEncoding=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
    }

    /**
     * 获取本地服务器IP地址
     */
    public static String getHostIp() {
        return HOST_IP;
    }

    private static final String HOST_IP = getHostIp0();

    private static String getHostIp0() {
        String hostIp = System.getProperty("mysql.servers");
        if (!StringUtil.isEmpty(hostIp)) {
            return hostIp;
        }
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            return "192.168.3.101";
        }

        String result = null;
        //String hostname = getHostName();
        String hostname = null;
        if (!StringUtil.isEmpty(hostname)) {
            try {
                InetAddress addr = InetAddress.getByName(hostname);
                if (!addr.isLoopbackAddress() && !addr.isMulticastAddress()) {
                    result = addr.getHostAddress();
                }
            } catch (Throwable t) {
            }
        }

        if (result == null) {
            // 获取第一个不是127.0.0.1的地址
            List<String> ipv4Addrs = new ArrayList<>();
            List<String> ipv6Addrs = new ArrayList<>();
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface nic = en.nextElement();
                    for (Enumeration<InetAddress> en2 = nic.getInetAddresses(); en2.hasMoreElements(); ) {
                        InetAddress addr = en2.nextElement();
                        if (addr.isAnyLocalAddress() || addr.isLoopbackAddress() || addr.isMulticastAddress()) {
                            continue;
                        }
                        if (addr instanceof Inet4Address) {
                            ipv4Addrs.add(addr.getHostAddress());
                        } else if (addr instanceof Inet6Address) {
                            ipv6Addrs.add(addr.getHostAddress());
                        }
                    }
                }
            } catch (Throwable t) {
            }
            if (ipv4Addrs.size() > 0) {
                result = ipv4Addrs.get(0);
            } else if (ipv6Addrs.size() > 0) {
                result = ipv6Addrs.get(0);
            }
        }
        return result;
    }
}
