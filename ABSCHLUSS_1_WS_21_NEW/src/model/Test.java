package model;

import exception.ParseException;

import java.io.StreamTokenizer;
import java.util.List;

public class Test {

    public static void main(String[] args) throws ParseException{

        IP root = new IP("141.255.1.133");
        List<List<IP>> levels = List.of(List.of(root),
                List.of(new IP("0.146.197.108"), new IP("122.177.67.158")));
        final Network network = new Network(root, levels.get(1));

        System.out.println("toString");
        System.out.println(network.toString(root));
        IP newRoot = new IP("122.177.67.158");
        System.out.println(network.toString(newRoot));
        IP newestRoot = new IP("0.146.197.108");
        System.out.println(network.toString(newestRoot));
        System.out.println("getheight");
        System.out.println((levels.size() - 1) == network.getHeight(root));
        System.out.println("contains");
        System.out.println(network.contains(root));
        System.out.println("levels");
        System.out.println(levels.equals(network.getLevels(root)));
        System.out.println("route");
        System.out.println(network.getRoute(root, newestRoot));
        System.out.println(network.getRoute(newRoot, newestRoot));
        System.out.println("list");
        System.out.println(network.list());
        System.out.println("disconnect:");
        System.out.println(network.disconnect(root, newestRoot));
        System.out.println(network.toString(root));

    }



}
