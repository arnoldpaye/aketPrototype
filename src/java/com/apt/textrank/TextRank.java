package com.apt.textrank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class TextRank.java (UTF-8)
 * @date 25/07/2013
 * @author Arnold Paye
 */
public class TextRank {

    static Logger log = Logger.getLogger(TextRank.class);
    public final static double MIN_NORMALIZED_RANK = 0.05;
    public final static int MAX_NGRAM_LENGTH = 5;

    public Graph buildGraph(String tex, String path, List<String> posFilterList) throws IOException {
        log.debug("POS_FILTER_LIST " + posFilterList);
        Language language = Language.buildLanguage(path);
        Graph graph = new Graph();
        for (String splitText : language.splitParagraph(tex)) {
            Sentence sentence = new Sentence(splitText);
            sentence.mapTokens(language, graph,posFilterList);
            graph.getSentenceList().add(sentence);
        }
        return graph;
    }

    public Map<NGram, MetricVector> init(Graph graph, String path) throws IOException {
        Map<NGram, MetricVector> metricSpace = new HashMap<NGram, MetricVector>();
        Language language = Language.buildLanguage(path);
        Graph ngramSubgraph;
        log.debug(graph.toString());
        log.info("\t\t\t" + "CONSTRUCT_GRAPH");
        // Pass 2: run TextRank to determine keywords****************************************************
        int maxResults = (int) Math.round((double) graph.size() * Graph.KEYWORD_REDUCTION_FACTOR);
//        log.debug("maxResults: " + maxResults);
        graph.runTextRank();
        graph.sortResults(maxResults);
        log.debug("GraphRankThreshold " + graph.getRankThreshold());
        ngramSubgraph = NGram.collectNGrams(language, graph.getSentenceList(), graph.getRankThreshold());
//        log.debug(graph.toString());
        log.debug(ngramSubgraph.toString());
        log.info("\t\t\t" + "BASIC_TEXTRANK");
        // Pass 3: lemmatize selected keywords and phrases************************************************
        Graph synSetSubGraph = new Graph();
        // TODO: use WordNet?
        synSetSubGraph = SynsetLink.pruneGraph(synSetSubGraph, graph);
        // Augment the graph with n-grams adde as nodes
        for (Node node : ngramSubgraph.values()) {
            NGram gram = (NGram) node.getNodeValue();
            if (gram.getLenght() < MAX_NGRAM_LENGTH) {
                graph.put(node.getKey(), node);
                for (Node keywordNode : gram.getNodes()) {
                    node.connect(keywordNode);
                }
            }
        }
//        log.debug("Graph\n");
//        log.debug(graph.toString());
//        log.debug("NGramSubGraph\n");
//        log.debug(ngramSubgraph.toString());
        log.info("\t\t\t" + "AUGMENT_GRAPH");
        // Pass 4: re-run TextRank on the augmented graph**************************************************
        graph.runTextRank();
        int nGramMaxCount = NGram.calcStats(ngramSubgraph);
        log.info("\t\t\t" + "NGRAM_TEXTRANK");
        log.debug("RANK: " + ngramSubgraph.getDistStat());
        for (Node node : new TreeSet<Node>(ngramSubgraph.values())) {
            NGram gram = (NGram) node.getNodeValue();
            log.debug(gram.getCount() + " " + node.getRank() + " " + gram.text);
        }
        log.debug("RANK: " + synSetSubGraph.getDistStat());
        for (Node node : new TreeSet<Node>(synSetSubGraph.values())) {
            SynsetLink synsetLink = (SynsetLink) node.getNodeValue();
            log.info("emit: " + "--" + " " + node.getRank() + " " + "--");
        }
        // Pass 5: construct a metric space for overall ranking********************************************
        double linkMin = ngramSubgraph.getDistStat().getMin();
        double linkCoeff = ngramSubgraph.getDistStat().getMax() - ngramSubgraph.getDistStat().getMin();
        double countMin = 1;
        double countCoeff = (double) nGramMaxCount - 1;
        double synsetMin = synSetSubGraph.getDistStat().getMin();
        double synsetCoeff = synSetSubGraph.getDistStat().getMax() - synSetSubGraph.getDistStat().getMin();

        for (Node node : ngramSubgraph.values()) {
            NGram gram = (NGram) node.getNodeValue();
            if (gram.getLenght() < MAX_NGRAM_LENGTH) {
                double linkRank = (node.getRank() - linkMin) / linkCoeff;
                double countRank = (gram.getCount() - countMin) / countCoeff;
                double synsetRank = 0.0D; //TODO: use wordnet?
                MetricVector metricVector = new MetricVector(gram, linkRank, countRank, synsetRank);
                metricSpace.put(gram, metricVector);
            }
        }
        log.info("\t\t\t" + "NORMALIZE_RANKS");
//        log.debug("Results");
//        log.debug(metricSpace.toString());
        // Render results
//        TreeSet<MetricVector> keyPhraseList = new TreeSet<MetricVector>(metricSpace.values());
//        StringBuilder stringBuilder = new StringBuilder();
//        for (MetricVector mv : keyPhraseList) {
//            if (mv.getMetric() >= MIN_NORMALIZED_RANK) {
//                stringBuilder.append(mv.render()).append('\t').append(mv.getNodeValue().text).append('\n');
//            }
//        }
//        log.info("ANSWER");
//        log.info(stringBuilder.toString());
        return metricSpace;
    }
}
