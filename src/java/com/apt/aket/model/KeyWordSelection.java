package com.apt.aket.model;

/**
 * @project aketPrototype
 * @package com.apt.aket.model
 * @class WordSelection.java (UTF-8)
 * @date 22/07/2013
 * @author Arnold Paye
 */
public class KeyWordSelection implements Comparable<KeyWordSelection> {

    private String value;
    private Double rank;

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

    public KeyWordSelection(String value, Double rank) {
        this.value = value;
        this.rank = rank;
    }

    @Override
    public int compareTo(KeyWordSelection t) {
        if (this.rank > t.rank) {
            return 0;
        } else {
            return 1;
        }
    }
}