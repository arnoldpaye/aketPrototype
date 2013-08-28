package com.apt.aket.data;

/**
 * @project aketPrototype
 * @package com.apt.aket.data
 * @class KeywordSelection.java (UTF-8)
 * @date 28/08/2013
 * @author Arnold Paye
 */
public class KeywordSelection implements Comparable<KeywordSelection> {

    /* Members */
    private String value;
    private Double rank;

    /* Getters and Setters */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    /**
     * Constructor with two parameters.
     *
     * @param value
     * @param rank
     */
    public KeywordSelection(String value, Double rank) {
        this.value = value;
        this.rank = rank;
    }

    /**
     *
     * @param that
     * @return
     */
    @Override
    public int compareTo(KeywordSelection that) {
        if (this.rank > that.rank) {
            return 0;
        } else {
            return 1;
        }
    }
}