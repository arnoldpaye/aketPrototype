package com.apt.aket.model;

/**
 * Project: aketPrototype
 * Package: com.apt.aket.model
 * Class  : Word.java (UTF-8)
 * 
 * @date 06/07/2013
 * @author Arnold Paye
 */
public class Word {
    private String word;
    private double rank;
    
    public Word(String word, Double rank) {
        this.word = word;
        this.rank = rank;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }
}
