package model.parse;

import exception.ParseException;
import model.NetworkTree;
import resources.ErrorMessage;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParserExecute implements IpParser, NetworkParser {

        public static final String REGEX_BIT = "(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])";
        public static final String IPV4_PATTERN = "%s.%s.%s.%s";
        public static final String REGEX_IPV4_ADDRESS = String.format(IPV4_PATTERN,
                                        REGEX_BIT, REGEX_BIT, REGEX_BIT, REGEX_BIT);

        public static final String IP_SPLITTER = "\\.";
        public static final String SUBNETWORK_OPENING = "(";
        public static final String SUBNETWORK_CLOSING = ")";
        public static final String SEPARATOR = " ";

    @Override
    public int[] parseAddress(String ipAddress) throws ParseException {
        if (!ipAddress.matches(REGEX_IPV4_ADDRESS)) throw new ParseException(ErrorMessage.ILLEGAL_FORMAT.toString());

        return Arrays.stream(ipAddress.split(IP_SPLITTER)).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public int[] assertRightfulParse(String ipAddress) {
        return Arrays.stream(ipAddress.split(IP_SPLITTER)).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public List<NetworkTree> parseNetwork(String subNetwork) throws ParseException {
        var counter = 0;
//        return parseNetwork(subNetwork, counter);
        return new LinkedList<>();
    }

    private NetworkTree parseNetwork(String subNetwork, int counter) throws ParseException {



        return new NetworkTree("");

    }


}
