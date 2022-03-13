package model.parse;

import exception.ParseException;
import model.NetworkTree;

import java.util.List;

public interface NetworkParser {

    List<NetworkTree> parseNetwork(String subNetwork) throws ParseException;

}
