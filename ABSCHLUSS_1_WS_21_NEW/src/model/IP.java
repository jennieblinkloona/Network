package model;

import exception.ParseException;
import model.parse.ParserExecute;

import java.util.Objects;

public class IP implements Comparable<IP>{

    private String ipv4Address;
    private final int[] bits;


    public IP(final String pointNotation) throws ParseException {
        this.ipv4Address = pointNotation;
        this.bits = new ParserExecute().parseAddress(pointNotation);
    }

    public IP(IP ip) {
        this.ipv4Address = ip.getIpv4Address();
        this.bits = new ParserExecute().assertRightfulParse(getIpv4Address());
    }

    public int[] getBits() {
        return bits;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    @Override
    public String toString() {
        return this.ipv4Address;
    }

    @Override
    public int compareTo(IP ip) {
        var thisSum = 0;
        var otherSum = 0;
        var power = 24;
        for (int i = 0; i < bits.length; i++){
            thisSum += (int) (bits[i] * Math.pow(2, power));
            otherSum += (int) (ip.getBits()[i] * Math.pow(2, power));
            power -= 8;
        }
        return Integer.compare(thisSum, otherSum);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IP ip = (IP) o;

        return Objects.equals(ipv4Address, ip.ipv4Address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ipv4Address);
    }

}
