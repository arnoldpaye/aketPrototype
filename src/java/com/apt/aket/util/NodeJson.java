package com.apt.aket.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @project aketPrototype
 * @package com.apt
 * @class NodeJson.java (UTF-8)
 * @date 13/08/2013
 * @author Arnold Paye
 */
public class NodeJson {

    /* Members */
    private int id;
    private String label;
    private List<NodeJson> nodeList;

    /* Getters and Setters */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<NodeJson> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<NodeJson> nodeList) {
        this.nodeList = nodeList;
    }

    /**
     * Constructor with two parameters.
     *
     * @param id
     * @param label
     */
    public NodeJson(int id, String label) {
        this.id = id;
        this.label = label;
        nodeList = new ArrayList<NodeJson>();
    }
}