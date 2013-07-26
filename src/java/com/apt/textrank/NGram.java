package com.apt.textrank;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class NGram.java (UTF-8)
 * @date 26/07/2013
 * @author Arnold Paye
 */
public class NGram extends NodeValue {

    static Logger log = Logger.getLogger(NGram.class);
    private HashSet<Node> nodes;
    private HashSet<Context> contexts = new HashSet<Context>();
    private int lenght;

    public int getLenght() {
        return lenght;
    }

    public HashSet<Node> getNodes() {
        return nodes;
    }

    private NGram(String text, HashSet<Node> nodes, Context context) {
        this.text = text;
        this.nodes = nodes;
        this.lenght = nodes.size();
        this.contexts.add(context);
    }

    @Override
    public String getDescription() {
        return "NGRAM" + '\t' + getCollocation();
    }

    public int getCount() {
        return contexts.size();
    }

    public static NGram buildNGram(Graph graph, Sentence sentence, LinkedList<Integer> tokenSpan, double maxRank) {
        HashSet<Node> nodes = new HashSet<Node>();
        StringBuffer sbKey = new StringBuffer("NGram");
        StringBuffer sbText = new StringBuffer();
        for (int i : tokenSpan) {
            if (!"".equals(sentence.getTokenList()[i])) {
                nodes.add(sentence.getNodeList()[i]);
                sbKey.append(sentence.getNodeList()[i].getKey());
                sbText.append(sentence.getTokenList()[i]).append(' ');
            }
        }
        Context context = new Context(sentence, tokenSpan.get(0));
        String gramKey = sbKey.toString();
        Node node = graph.get(gramKey);
        NGram gram = null;
        if (node == null) {
            gram = new NGram(sbText.toString().trim(), nodes, context);
            if (!"".equals(gram.text.trim())) {
                node = Node.buildNode(graph, gramKey, gram);
                node.setRank(maxRank);
            }
        } else {
            gram = (NGram) node.getNodeValue();
        }
        return gram;
    }

    public static Graph collectNGrams(Language language, List<Sentence> sentenceList, double rankThreshold) {
        Graph graph = new Graph();
        LinkedList<Integer> tokenSpan = new LinkedList<Integer>();
        for (Sentence sentence : sentenceList) {
            boolean spanMarked = false;
            double maxRank = 0.0D;
            tokenSpan.clear();
            for (int i = 0; i < sentence.getNodeList().length; i++) {
                //log.debug("sentence " + i + " is null " + (sentence.getNodeList()[i] == null));
                if (sentence.getNodeList()[i] == null) {
                    log.debug("sentence: " + sentence.getNodeList()[i] + " " + spanMarked + " " + tokenSpan.size());
                    if (spanMarked && (tokenSpan.size() > 0)) {
                        if ((tokenSpan.size() > 1) || ((maxRank >= rankThreshold) && language.isNoun(((KeyWord) sentence.getNodeList()[tokenSpan.get(0)].getNodeValue()).getPos()))) {
                            NGram gram = buildNGram(graph, sentence, tokenSpan, maxRank);
                            log.debug("emit " + gram.text + " @ " + gram.getCount() + " span " + gram.lenght);
                        }
                    }
                    tokenSpan.clear();
                    spanMarked = false;
                    maxRank = 0.0D;
                } else {
                    tokenSpan.add(i);
                    spanMarked = spanMarked || sentence.getNodeList()[i].isMarked();
                    maxRank = Math.max(maxRank, sentence.getNodeList()[i].getRank());
                }

            }
        }
        return graph;
    }

    public static int calcStats(Graph graph) {
        int ngramMaxCount = 2;
        graph.getDistStat().clear();
        for (Node node : graph.values()) {
            NGram gram = (NGram) node.getNodeValue();
            graph.getDistStat().addValue(node.getRank());
            ngramMaxCount = Math.max(gram.getCount(), ngramMaxCount);
//            log.debug(node.getNodeValue().text + " " + ngramMaxCount);
        }
        return ngramMaxCount;
    }
}
