package model.parse;

import exception.ParseException;

public interface IpParser {


    int[] parseAddress(String  ipAddress) throws ParseException;

    int[] assertRightfulParse(String ipAddress);
}
