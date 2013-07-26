package com.apt.textrank;

import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class SynsetLink.java (UTF-8)
 * @date 26/07/2013
 * @author Arnold Paye
 */
public class SynsetLink extends NodeValue {

    static Logger log = Logger.getLogger(SynsetLink.class);
    private Node parent;

    public static Graph pruneGraph(Graph subgraph, Graph graph) {
        Graph newSubGraph = new Graph();
        for (Node node : subgraph.values()) {
            if (node.isMarked()) {
                graph.put(node.getKey(), node);
                newSubGraph.put(node.getKey(), node);
            } else {
                SynsetLink synsetLink = (SynsetLink) node.getNodeValue();
                node.disconnect(synsetLink.parent);
            }
        }
        return newSubGraph;
    }
}
