package com.apt.textrank;

import java.util.HashSet;
import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class Node.java (UTF-8)
 * @date 25/07/2013
 * @author Arnold Paye
 */
public class Node implements Comparable<Node> {

    static Logger log = Logger.getLogger(Node.class);
    private HashSet<Node> edges = new HashSet<Node>();
    private double rank;
    private String key;
    private NodeValue nodeValue;
    private boolean marked;

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public String getKey() {
        return key;
    }

    public HashSet<Node> getEdges() {
        return edges;
    }

    public NodeValue getNodeValue() {
        return nodeValue;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public Node(String key, NodeValue nodeValue) {
        this.rank = 1.0D;
        this.key = key;
        this.nodeValue = nodeValue;
        this.marked = false;
    }

    public static Node buildNode(Graph graph, String key, NodeValue nodeValue) {
        Node node = graph.get(key);
        if (node == null) {
            node = new Node(key, nodeValue);
            graph.put(key, node);
        }
        log.debug(node.getKey());
        return node;
    }

    public void connect(Node that) {
        this.edges.add(that);
        that.edges.add(this);
    }
    
    public void disconnect(Node that) {
        this.edges.remove(that);
        that.edges.remove(this);
    }

    @Override
    public String toString() {
        return rank + " " + key + " " + nodeValue.getDescription();
    }
    
    @Override
    public int compareTo(Node that) {
        if (this.rank > that.rank) {
            return -1;
        } else if (this.rank < that.rank) {
            return 1;
        } else {
            return this.getNodeValue().text.compareTo(that.getNodeValue().text);
        }
    }
}