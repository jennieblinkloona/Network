package model;

import exception.ParseException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public class NetworkTree {

    private NetworkNode root;

    public NetworkTree(String input) throws ParseException {
        this.root = generateTree(input , null, 0);
    }

    public NetworkTree(IP root, List<IP> children) {
        this.root = new NetworkNode(root);
        children.forEach(child -> this.root.getConnectedNotes().add(new NetworkNode(child)));
        this.root.getConnectedNotes().forEach(child -> child.getConnectedNotes().add(this.root));
    }

    public NetworkTree(NetworkNode root) {
        this.root = root;
    }

    public NetworkTree(NetworkTree tree) {
        this.root = new NetworkNode(tree.getRoot());
    }

    private NetworkNode generateTree(String input, NetworkNode root, int index) throws ParseException {
        return new NetworkNode(new IP(input));
    }

    public NetworkNode getRoot() {
        return root;
    }

    public boolean hasBoth(IP ip1, IP ip2) {
        return contains(root, ip1, new ArrayList<>()) && contains(root, ip2, new ArrayList<>());
    }

    public void connect(IP firstIP, IP secondIP) {
        var node1 = locate(this.root, firstIP, new ArrayList<>());
        var node2 = locate(this.root, secondIP, new ArrayList<>());
        node1.getConnectedNotes().add(node2);
        node2.getConnectedNotes().add(node1);
    }

    public NetworkTree disconnect(IP firstIP, IP secondIP) {
        var node1 = locate(this.root, firstIP, new ArrayList<>());
        var node2 = locate(this.root, secondIP, new ArrayList<>());
        return disconnect(this.root, node1.equals(this.root)?node1:node2 , node1.equals(this.root)?node2:node1);
    }

    private NetworkTree disconnect(NetworkNode root, NetworkNode keeper, NetworkNode disconnector) {
        var disconnect = keeper.getConnectedNotes().stream().filter(node -> node.equals(disconnector)).findFirst().orElse(null);
        var newTree = new NetworkTree(disconnect);
        keeper.getConnectedNotes().remove(disconnect);
        assert disconnect != null;
        disconnect.getConnectedNotes().remove(keeper);

        return newTree;
    }

    public boolean disconnectable(IP ip1, IP ip2) {
        var node1 = locate(this.root, ip1, new ArrayList<>());
        var node2 = locate(this.root, ip2, new ArrayList<>());
        return node1.getConnectedNotes().contains(node2);
    }

    public boolean contains(IP ipToFind) {
        return contains(this.root, ipToFind, new ArrayList<>());
    }

    private boolean contains(NetworkNode root, IP ipToFind, List<NetworkNode> duplicates) {
        if (root.getIpNode().equals(ipToFind)) return true;
        duplicates.add(root);
        for (NetworkNode child : root.getConnectedNotes()) {
            if (duplicates.contains(child)) continue;
            duplicates.add(child);
            if (contains(child, ipToFind, duplicates)) return true;
        }
        return false;
    }

    public int getHeight(IP root){
       return getHeight(Objects.requireNonNull(locate(this.root, root, new ArrayList<>())), new ArrayList<>()) - 1;
    }

    public NetworkNode locate(NetworkNode node, IP ipToFind, List<NetworkNode> duplicates) {
        if (node.getIpNode().equals(ipToFind)) return node;
        duplicates.add(node);
        for (NetworkNode child : node.getConnectedNotes()) {
            if (duplicates.contains(child)) continue;
            duplicates.add(child);
            if (child.getIpNode().equals(ipToFind)) return child;
            locate(child, ipToFind, duplicates);
        }
        return null;
    }

    public List<IP> getList()  {
        var listOfIps = new ArrayList<IP>();
        return getList(this.root, listOfIps, new ArrayList<>());
    }

    private List<IP> getList(NetworkNode root, ArrayList<IP> ipList, List<NetworkNode> duplicates) {
        if (root.getIpNode().equals(this.root.getIpNode())) ipList.add(this.root.getIpNode());
        duplicates.add(root);
        for (NetworkNode child : root.getConnectedNotes()) {
            if (duplicates.contains(child)) continue;
            duplicates.add(child);
            ipList.add(child.getIpNode());
            getList(child, ipList, duplicates);
        }
        return ipList;
    }

    private int getHeight(NetworkNode node, List<NetworkNode> duplicates) {
        var height = 0;
        duplicates.add(node);
        for (NetworkNode child : node.getConnectedNotes()) {
            if (duplicates.contains(child)) continue;
            duplicates.add(child);
            var childHeight = getHeight(child, duplicates);
            if (childHeight > height) height = childHeight;
        }
        return height + 1;
    }

    public List<IP> getPathBetweenNodes(IP startIP, IP endIP) {
        return getPathBetweenNodes(this.root, startIP, endIP);
    }

    private List<IP> getPathBetweenNodes(NetworkNode node, IP startIP, IP endIP) {

        var firstPath = findPath(node, startIP, new ArrayList<>());
        var secondPath = findPath(node, endIP, new ArrayList<>());

        var commonNode = firstPath.stream().filter(secondPath::contains).collect(Collectors.toList()).stream().findFirst().orElse(null);

        var path = new ArrayList<IP>();

        for (IP ip : firstPath) {
            if (ip.equals(commonNode)) {
                path.add(ip);
                break;
            }
            path.add(ip);
        }

        var found = false;
        for (int i = secondPath.size() - 1; i >= 0; i--) {
            if (found) path.add(secondPath.get(i));
            if (!found) if (secondPath.get(i).equals(commonNode)) found = true;
        }

        return path;
    }

    private ArrayList<IP> findPath(NetworkNode node, IP ip, List<NetworkNode> duplicates) {
        if (node.getIpNode().equals(ip)) {
            var path = new ArrayList<IP>();
            path.add(node.getIpNode());
            return path;
        }
        duplicates.add(node);

        for (NetworkNode child : node.getConnectedNotes()) {
            if (duplicates.contains(child)) continue;
            duplicates.add(child);
            var path = findPath(child, ip, duplicates);
            if (!path.isEmpty()) {
                path.add(node.getIpNode());
                return path;
            }
        }
        return new ArrayList<>();
    }

    public List<List<IP>> getLevels(IP root) {
        return getLevels(locate(this.root, root, new ArrayList<>()), new ArrayList<>(), new ArrayList<>());
    }

    private List<List<IP>> getLevels(NetworkNode node, ArrayList<List<IP>> nestedList, List<NetworkNode> duplicates) {
        if (node == null) return new ArrayList<>();

        Queue<NetworkNode> queue = new LinkedList<>();
        queue.add(node);
        duplicates.add(node);
        var index = 0;
        while (!queue.isEmpty()) {
            var counter = queue.size();
            nestedList.add(new ArrayList<>());

            while (counter > 0) {
                var item = queue.poll();
                assert item != null;
                nestedList.get(index).add(new IP(item.getIpNode()));
                var listWithoutDuplicates = new ArrayList<>(item.getConnectedNotes());
                duplicates.forEach(listWithoutDuplicates::remove);
                queue.addAll(listWithoutDuplicates);
                counter--;
            }
            index++;
        }
        return nestedList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkTree that = (NetworkTree) o;
        return Objects.equals(root, that.root);
    }

    @Override
    public int hashCode() {
        return root != null ? root.hashCode() : 0;
    }

    public String toString(IP root) {
        return toStringBuilder(Objects.requireNonNull(locate(this.root, root, new ArrayList<>())), new StringBuilder(), new ArrayList<>());
    }

    private String toStringBuilder(NetworkNode root, StringBuilder builder, List<NetworkNode> duplicates) {
        builder.append("(").append(root.getIpNode()).append(" ");
        duplicates.add(root);
        for (NetworkNode node : root.getConnectedNotes()) {
            if (duplicates.contains(node)) continue;
            duplicates.add(node);
            if (node.getConnectedNotes().size() == 1 && node.getConnectedNotes().contains(root)) builder.append(node.getIpNode()).append(" ");
            else builder.append(toStringBuilder(node, new StringBuilder(), duplicates));
        }
        return builder.deleteCharAt(builder.length() - 1).append(")").toString();
    }

    public void mergeTree(NetworkNode root , NetworkTree subnet, List<NetworkNode> duplicates) {
       if (subnet.contains(root.getIpNode())) mergeTree(root, subnet.locate(subnet.getRoot(), root.getIpNode(), new ArrayList<>()), new ArrayList<>());
       duplicates.add(root);
       for (NetworkNode node : root.getConnectedNotes()) {
           if (duplicates.contains(node)) continue;
           mergeTree(node, subnet, duplicates);
       }
    }

    private void mergeTree(NetworkNode root, NetworkNode node, List<NetworkNode> duplicates) {
        duplicates.add(node);
        for (NetworkNode child : node.getConnectedNotes()) {
            if (duplicates.contains(child)) continue;
            if (root.getConnectedNotes().contains(child)) mergeTree(locate(this.root, child.getIpNode(), new ArrayList<>()), child, duplicates);
            var newNode = new NetworkNode(child);
            root.getConnectedNotes().add(newNode);
        }
    }

    public boolean isAddable(NetworkTree subTree) {
        return isAddable(this.root, subTree, new ArrayList<>());
    }

    private boolean isAddable(NetworkNode root, NetworkTree subTree, List<NetworkNode> duplicates) {
        duplicates.add(root);
        if (subTree.contains(root.getIpNode())) {
            var node = subTree.locate(subTree.getRoot(), root.getIpNode(), new ArrayList<>());
            for (NetworkNode child : root.getConnectedNotes()) {
                if (!node.getConnectedNotes().contains(child) && subTree.contains(child.getIpNode())) return false;
            }
            for (NetworkNode child : node.getConnectedNotes()) {
                if (!root.getConnectedNotes().contains(child) && !isNewBranch(root, child)) return false;
            }
        }
        for (NetworkNode child : root.getConnectedNotes()) {
            if (duplicates.contains(child)) continue;
            if (!isAddable(child, subTree, duplicates)) return false;
        }
        return true;
    }

    private boolean isNewBranch(NetworkNode root, NetworkNode node) {
        if (contains(node.getIpNode())) return false;
        for (NetworkNode child : node.getConnectedNotes()) {
            if (child.equals(root)) continue;
            if (!isNewBranch(root, child)) return false;
        }
        return true;
    }

    public boolean isMergeable(NetworkTree subTree){
        return isMergeable(this.root, subTree, new ArrayList<>());
    }

    private boolean isMergeable(NetworkNode root, NetworkTree subTree, List<NetworkNode> duplicates) {
        if (subTree.contains(root.getIpNode())) return true;
        duplicates.add(root);
        for (NetworkNode node : root.getConnectedNotes()) {
            if (duplicates.contains(node)) continue;
            if (isMergeable(node, subTree, duplicates)) return true;
        }
        return false;
    }

}
