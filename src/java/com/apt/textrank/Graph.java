package com.apt.textrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class Graph.java (UTF-8)
 * @date 25/07/2013
 * @author Arnold Paye
 */
public class Graph extends TreeMap<String, Node> {

    static Logger log = Logger.getLogger(Graph.class);
    public final static double INCLUSIVE_COEFF = 0.25D;
    public final static double KEYWORD_REDUCTION_FACTOR = 0.8D;
    public final static double TEXTRANK_DAMPING_FACTOR = 0.85D;
    public final static double STANDARD_ERROR_THRESHOLD = 0.005D;
    private List<Sentence> sentenceList;
    private SummaryStatistics distStat;
    private Node[] nodeList;

    public List<Sentence> getSentenceList() {
        return sentenceList;
    }

    public SummaryStatistics getDistStat() {
        return distStat;
    }

    public Graph() {
        sentenceList = new ArrayList<Sentence>();
        distStat = new SummaryStatistics();
    }

    private void iterateGraph(int maxIterations) {
        double[] rankList = new double[nodeList.length];
        for (int k = 0; k < maxIterations; k++) {
            distStat.clear();
            for (int i = 0; i < nodeList.length; i++) {
                Node node = nodeList[i];
                double rank = 0.0D;
                for (Node internalNode : node.getEdges()) {
                    rank += internalNode.getRank() / (double) internalNode.getEdges().size();
                }
                rank *= TEXTRANK_DAMPING_FACTOR;
                rank += 1.0D - TEXTRANK_DAMPING_FACTOR;
                rankList[i] = rank;
                distStat.addValue(Math.abs(node.getRank() - rank));
            }
            double standardError = distStat.getStandardDeviation() / Math.sqrt((double) distStat.getN());
            log.info("iteration: " + k + " error: " + standardError);
            for (int i = 0; i < nodeList.length; i++) {
                nodeList[i].setRank(rankList[i]);
            }
            if (standardError < STANDARD_ERROR_THRESHOLD) {
//                log.debug("End of iteration in: " + k + " with: " + distStat.toString());
                break;
            }
        }
    }

    public void runTextRank() {
        int maxIterations = this.size();
        nodeList = new Node[this.size()];
        int i = 0;
        for (Node node : this.values()) {
            nodeList[i++] = node;
        }
        iterateGraph(maxIterations);
    }

    public void sortResults(long maxResults) {
        Arrays.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                if (n1.getRank() > n2.getRank()) {
                    return -1;
                } else if (n1.getRank() < n2.getRank()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        distStat.clear();
        for (int i = 0; i < nodeList.length; i++) {
            Node node = nodeList[i];
            if (i <= maxResults) {
                node.setMarked(true);
                distStat.addValue(node.getRank());
            }
        }
    }

    public double getRankThreshold() {
        return distStat.getMean() + (distStat.getStandardDeviation() * INCLUSIVE_COEFF);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        for (Node node : values()) {
            stringBuilder.append(node.toString()).append('\n');
            for (Node internalNode : node.getEdges()) {
                stringBuilder.append('\t').append(internalNode.toString()).append('\n');
            }
        }
        return stringBuilder.toString();
    }
    
    public String renderHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : values()) {
            stringBuilder.append("<h3>");
            stringBuilder.append(node.getNodeValueText()).append("-");
            stringBuilder.append(((KeyWord)node.getNodeValue()).getPos());
            stringBuilder.append("</h3>");
            for (Node internalNode : node.getEdges()) {
                stringBuilder.append("(").append(internalNode.getNodeValueText()).append(")");
            }
            stringBuilder.append("</ br>");
        }
        return stringBuilder.toString();
    }
}