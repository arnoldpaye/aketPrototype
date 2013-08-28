package com.apt.aket.model;

import org.openlogics.cjb.jdbc.annotation.Column;

/**
 * @project aketPrototype
 * @package com.apt.aket.model
 * @class Keyword.java (UTF-8)
 * @date 09/08/2013
 * @author Arnold Paye
 */
public class Keyword {

    /* Members */
    @Column("kw_id")
    private int kwId;
    @Column("kw_txt_id")
    private int kwTxtId;
    @Column("kw_value")
    private String kwValue;
    @Column("kw_source")
    private int kwSource;

    /* Getters and Setters */
    public int getKwId() {
        return kwId;
    }

    public void setKwId(int kwId) {
        this.kwId = kwId;
    }

    public int getKwTxtId() {
        return kwTxtId;
    }

    public void setKwTxtId(int kwTxtId) {
        this.kwTxtId = kwTxtId;
    }

    public String getKwValue() {
        return kwValue;
    }

    public void setKwValue(String kwValue) {
        this.kwValue = kwValue;
    }

    public int getKwSource() {
        return kwSource;
    }

    public void setKwSource(int kwSource) {
        this.kwSource = kwSource;
    }

    /**
     * Default constructor.
     */
    public Keyword() {
    }

    /**
     * Constructor with two parameters.
     *
     * @param kwTxtId
     * @param kwValue
     * @param kwSource
     */
    public Keyword(int kwTxtId, String kwValue, int kwSource) {
        this.kwTxtId = kwTxtId;
        this.kwValue = kwValue;
        this.kwSource = kwSource;
    }
}