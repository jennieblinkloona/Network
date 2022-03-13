package model;

import exception.NetworkParseException;
import exception.ParseException;
import model.parse.ParserExecute;
import resources.ErrorMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Network {

    private final List<NetworkTree> networkTrees;

    public Network(final IP root, final List<IP> children) {
        if (children.isEmpty()) throw new NetworkParseException(ErrorMessage.ILLEGAL_CHILDREN_LIST_SIZE.toString());
        if (children.contains(root)) throw new NetworkParseException(ErrorMessage.ILLEGAL_CHILDREN_LIST_ROOT.toString());
        networkTrees = new ArrayList<>();
        networkTrees.add(new NetworkTree(root, children));
    }

    public Network(final String bracketNotation) throws ParseException {
        var parsedNetwork = new ParserExecute().parseNetwork(bracketNotation);
        networkTrees = new ArrayList<>();
//        networkTrees.add(parsedNetwork);
    }

    public boolean add(final Network subnet) {

        for (NetworkTree subTree : subnet.getNetworkTrees()) {
            for (NetworkTree tree : networkTrees) {
                if (!tree.isAddable(subTree)) return false;
            }
        }

        for (NetworkTree subTree : subnet.getNetworkTrees()) {
            var merged = false;
            for (NetworkTree tree : networkTrees) {
                if (!tree.isMergeable(subTree)) continue;
                tree.mergeTree(tree.getRoot(), subTree, new ArrayList<>());
                merged = true;
            }
            if (!merged) networkTrees.add(new NetworkTree(subTree));
        }
        rearrangeNetworkTrees();
        return true;
    }

    private void rearrangeNetworkTrees() {
        for (NetworkTree tree : networkTrees) {
            for (NetworkTree otherTree : networkTrees) {
                if (tree.equals(otherTree) || !tree.isMergeable(otherTree)) continue;
                tree.mergeTree(tree.getRoot(), otherTree, new ArrayList<>());
                networkTrees.remove(otherTree);
            }
        }
    }

    public List<IP> list() {
        //return all ips without duplicates and ordered
        var listOfAllIps = new ArrayList<IP>();
        networkTrees.forEach(networkTree -> listOfAllIps.addAll(networkTree.getList()));
        Collections.sort(listOfAllIps);
        return listOfAllIps;
    }

    public boolean connect(final IP ip1, final IP ip2) {
        //assert both ips are existent
        if  (!contains(ip1) || !contains(ip2)) return false;

        var firstIP = networkTrees.stream().filter(networkTree -> networkTree.contains(ip1)).findFirst().orElse(null);
        var secondIP = networkTrees.stream().filter(networkTree -> networkTree.contains(ip2)).findFirst().orElse(null);

        assert firstIP != null;
        assert secondIP != null;

        if (firstIP.equals(secondIP)) return false;

        firstIP.connect(ip2, ip1);
        networkTrees.remove(secondIP);
        return true;
    }

    public boolean disconnect(final IP ip1, final IP ip2) {
        //assert that both ips are existent
        if (!contains(ip1) || !contains(ip2)) return false;

        var targetTree = networkTrees.stream().filter(networkTree -> networkTree.hasBoth(ip1, ip2)).findFirst().orElse(null);

        if (targetTree == null) return false;
        //checking if it would leave the network with no valid trees anymore
        if (targetTree.getHeight(targetTree.getRoot().getIpNode()) <= 1
                && targetTree.getRoot().getConnectedNotes().size() <= 1
                && networkTrees.size() == 1) return false;

        if (!targetTree.disconnectable(ip1, ip2)) return false;

        var newTree = targetTree.disconnect(ip1, ip2);

        if (getHeight(newTree.getRoot().getIpNode()) != 0) networkTrees.add(newTree);
        if (getHeight(targetTree.getRoot().getIpNode()) < 1) networkTrees.remove(targetTree);
        return true;
    }

    public boolean contains(final IP ip) {
        return networkTrees.stream().anyMatch(networkTree -> networkTree.contains(ip));
    }

    public int getHeight(final IP root) {
        return networkTrees.stream().filter(networkTree -> networkTree.contains(root))
                .map(networkTree -> networkTree.getHeight(root)).findFirst().orElse(0);
    }

    public List<List<IP>> getLevels(final IP root) {
        //assert that ip os existent

        if (!contains(root)) return new ArrayList<>();
        var targetTree = networkTrees.stream().filter(networkTree -> networkTree.contains(root)).findFirst().orElse(null);
        if (targetTree == null) return new ArrayList<>();
        return targetTree.getLevels(root);
    }

    public List<IP> getRoute(final IP start, final IP end) {
        return networkTrees.stream().filter(networkTree -> networkTree.contains(start))
                .filter(networkTree -> networkTree.contains(end))
                .map(networkTree -> networkTree.getPathBetweenNodes(start, end)).findFirst().orElse(Collections.emptyList());

    }

    public String toString(IP root) {
        return networkTrees.stream()
                .filter(networkTree -> networkTree.contains(root))
                .map(networkTree -> networkTree.toString(root)).findFirst().orElse("");
    }

    public List<NetworkTree> getNetworkTrees() {
        return networkTrees;
    }
}
