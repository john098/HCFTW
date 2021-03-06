package com.example.johnpham.hcftw;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Jake on 3/9/2015.
 */
public class Report {

    private String startdate;//stores the start time of the report
    private String datetime;//stores the timeof completion
    private String ip; //stores user ipaddress
    private String name;//stores the user name
    private long phone;
    private String month;
    private String role; //stores the selected role
    private String teachhr; //stores how many hours spent teaching
    private String prephr; //stores how many hours spent preparing
    private String travel; //stores miles traveled
    private String servhr; //stores how many hours volunteering(serving)
    private String acomp;// stores the users accomplishments for the month

    /**
     * Initializes a Report sets the start date right away
     */
    public Report(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startdate = sdf.format(dt);
        ip=getIPAddress(true);
        name=" ";
        month ="Not Selected";
        role="A0";
        teachhr="A0";
        prephr="A0";
        travel="A0";
        servhr="A0";
        acomp="empty";
    }

    public void setStartdate(String sd){
        startdate=sd;
    }
    public void setDatetime(String dt){
        datetime=dt;
    }
    public void setIp(String ipAddr){
        ip=ipAddr;
    }
    public void setName(String newName){
        name=newName;
    }
    public void setPhone(long number){
        phone=number;
    }
    public void setMonth(String rMonth){
        month=rMonth;
    }

    public void setTeachhr(String teachhr) {
        this.teachhr = teachhr;
    }

    public void setPrephr(String prephr) {
        this.prephr = prephr;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public void setServhr(String servhr) {
        this.servhr = servhr;
    }

    public void setAcomp(String acomp) {
        this.acomp = acomp;
    }

    public void setRole(String role1){
        role=role1;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getDatetime() {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        datetime = sdf.format(dt);
        return datetime;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public long getPhone() {
        return phone;
    }

    public String getMonth() {
        return month;
    }

    public String getRole() {
        return role;
    }

    public String getTeachhr() {
        return teachhr;
    }

    public String getPrephr() {
        return prephr;
    }

    public String getTravel() {
        return travel;
    }

    public String getServhr() {
        return servhr;
    }

    public String getAcomp() {
        return acomp;
    }

    /**
     * Gets the IP address of the current user
     * @param useIPv4 //checks to see which ip type the current address is either ipv4 or ipv6
     * @return
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
}
