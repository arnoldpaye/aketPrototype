package com.apt.textrank;

import com.apt.aket.model.WordSelection;
import com.apt.aket.model.WordTag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class Util.java (UTF-8)
 * @date 22/07/2013
 * @author Arnold Paye
 */
public class Util {

    /**
     * Buid adjacency matrix of wordtags.
     *
     * @param words
     * @return
     */
    public static double[][] buildAdjacencyMatrix(List<WordSelection> wordSelections) {
        int size = wordSelections.size();
        double[][] graph = new double[size][size];
        // TODO: build graph in fuction of correlation.
        for (int i = 0; i < graph.length - 2; i++) {
            graph[i][i + 1] = 1;
            graph[i + 1][i] = 1;
            graph[i][i + 2] = 1;
            graph[i + 2][i] = 1;
        }
        return graph;
    }

    /**
     * Filter part-of-speech type.
     *
     * @param words
     * @return
     */
    public static List<WordSelection> filterPOS(List<WordTag> wordTags) {
        Set<WordSelection> setWordSelection = new HashSet<WordSelection>();
        List<WordSelection> wordSelections = new ArrayList<WordSelection>();
        if (wordTags != null && wordTags.size() > 0) {
            for (WordTag wordTag : wordTags) {
                // Nouns, verbs and adjectives
                if (isNoun(wordTag.getTag()) || isVerb(wordTag.getTag()) || isAdjective(wordTag.getTag())) {
                    setWordSelection.add(new WordSelection(wordTag.getValue().toUpperCase(), Double.valueOf(0)));
                }
            }
            for (WordSelection wordSelection : setWordSelection) {
                wordSelections.add(wordSelection);
            }
        }
        return wordSelections;
    }

    /**
     * Create the transition probability matrix Assumptions: A is a square
     * matrix, alpha is between 0 and 1
     *
     * @param A adjacency matrix of the graph
     * @param alpha probability of the teleport operation
     * @return
     */
    public static double[][] transitionProbabilityMatrix(double[][] A, double alpha) {
        double aux[] = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                // TODO: make a better comparison
                if (A[i][j] > 0) {
                    aux[i]++;
                }
            }
        }

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                if (aux[i] > 0) {
                    A[i][j] = ((A[i][j] / aux[i]) * (1 - alpha)) + (alpha / A.length);
                } else {
                    A[i][j] = 1 / A.length;
                }
            }
        }
        return A;
    }

    /**
     * *
     * PageRank algorithm based on the book "An Introduction to Information
     * Retrieval" of Christopher D. Manning et. al. Assumptions: A is a square
     * matrix
     *
     * @param A adjacency matrix of the graph
     * @return vector of pagerank
     */
    public static double[] pageRank(double[][] A) {
        double pr[] = new double[A.length];
        pr[0] = 1; // starting at node 0
        // Get the transition probabily matrix
        double P[][] = transitionProbabilityMatrix(A, 0.85);
        int cout = 0;
        while (true) {
            double aux[] = new double[pr.length];
            // Begin of the iteration
            for (int j = 0; j < P.length; j++) {
                for (int i = 0; i < P.length; i++) {
                    aux[j] += (pr[i] * P[i][j]);
                }
            }
            // End of the iteration
            if (!converges(aux, pr) && cout < 100) {
                // Print the iteration
                System.out.print("Iteration " + ++cout + ": ");
                for (int i = 0; i < pr.length; i++) {
                    //System.out.print(pr[i] + " ");
                    System.out.printf("%.2f ", pr[i]);
                }
                System.out.println();
                pr = aux;
            } else {
                break;
            }
        }
        return pr;
    }

    /**
     * Naive algorithm to check whether the iteration converges
     *
     * @param xt1
     * @param xt2
     * @return
     */
    private static boolean converges(double xt1[], double xt2[]) {
        double error = 0.000001;
        for (int i = 0; i < xt1.length; i++) {
            if (Math.abs(xt1[i] - xt2[i]) > error) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNoun(String tag) {
        return tag.startsWith("NC");
    }

    public static boolean isVerb(String tag) {
        return tag.startsWith("V");
    }

    public static boolean isAdjective(String tag) {
        return tag.startsWith("AQ");
    }
}