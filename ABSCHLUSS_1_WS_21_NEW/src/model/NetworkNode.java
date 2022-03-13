package model;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class NetworkNode implements Comparable<NetworkNode>{

    private final IP ipNode;
    private final Set<NetworkNode> connectedNotes;

    public NetworkNode(IP root) {
        this.ipNode = root;
        this.connectedNotes = new TreeSet<>();
    }

    public NetworkNode(NetworkNode node) {
        this.ipNode = new IP(node.getIpNode());
        this.connectedNotes = new TreeSet<>();
        node.getConnectedNotes().forEach(child -> this.connectedNotes.add(new NetworkNode(child.getIpNode())));
    }



    public IP getIpNode() {
        return ipNode;
    }

    public Set<NetworkNode> getConnectedNotes() {
        return connectedNotes;
    }

    @Override
    public int compareTo(NetworkNode o) {
        return ipNode.compareTo(o.getIpNode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkNode that = (NetworkNode) o;

        if (!Objects.equals(ipNode, that.ipNode)) return false;
        return connectedNotes.equals(that.connectedNotes);
    }

    @Override
    public int hashCode() {
        int result = ipNode != null ? ipNode.hashCode() : 0;
        result = 31 * result + connectedNotes.hashCode();
        return result;
    }
}
