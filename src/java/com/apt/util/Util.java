package com.apt.util;

import java.text.Collator;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 * @project aketPrototype
 * @package com.apt.textrank
 * @class Util.java (UTF-8)
 * @date 22/07/2013
 * @author Arnold Paye
 */
public class Util {

    static Logger log = Logger.getLogger(Util.class);

    /**
     * Validate the keyword based in the following format:
     * keyword1;keyword2;keyword3 and so on.
     *
     * @param keywords
     * @return
     */
    public static boolean validateKeywordsText(String keywords) {
        return true;
    }

    public static String deletePuntuationSigns(String text) {
        text = text.replace(".", "");
        text = text.replace(",", "");
        text = text.replace("(", "");
        text = text.replace(")", "");
        text = text.replace(";", "");
        return text;
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

    public static double[] evaluate(String[] keyWord, String[] keyWordTextRank) {
        System.out.println("Set of relevant items: " + keyWord.length);
//        for (String s : keyWordMac) {
//            System.out.println(s);
//        }
        System.out.println("Set of items retrieved: " + keyWordTextRank.length);
//        for (String s : keyWordTextRank) {
//            System.out.println(s);
//        }
        int itemsCounterEquals = 0;
        Collator collator = Collator.getInstance(new Locale("es", "ES"));
        collator.setStrength(Collator.PRIMARY);
        for (String s1 : keyWord) {
            for (String s2 : keyWordTextRank) {
                if (collator.compare(s1, s2) == 0) {
                    System.out.println(s1 + " is equals to " + s2);
                    itemsCounterEquals++;
                }
            }
        }
        System.out.println("itemsCounterEquals: " + itemsCounterEquals);
        int a = itemsCounterEquals;
        int b = keyWord.length - itemsCounterEquals;
        int c = keyWordTextRank.length - itemsCounterEquals;



        double[] e = new double[3];
        // precision************************************************************
        e[0] = (double) a / (a + c) * 100;
        // recall***************************************************************
        e[1] = (double) a / (a + b) * 100;
        // f-measure************************************************************
        e[2] = 2 * (e[0] * e[1]) / (e[0] + e[1]);
        return e;
    }
}