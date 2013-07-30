package com.apt.aket.model;

import org.openlogics.cjb.jdbc.annotation.Column;

/**
 * @project aketPrototype
 * @package com.apt.aket.model
 * @class Evaluation.java (UTF-8)
 * @date 30/07/2013
 * @author Arnold Paye
 */
public class Evaluation {

    @Column("ev_id")
    private int evId;
    @Column("ev_txt_id")
    private int evTxtId;
    @Column("txt_title")
    private String txtTitle;
    @Column("ev_precision")
    private double evPrecision;
    @Column("ev_recall")
    private double evRecall;
    @Column("ev_fmeasure")
    private double evFMeasure;

    //Getters and setters*******************************************************
    public int getEvId() {
        return evId;
    }

    public void setEvId(int evId) {
        this.evId = evId;
    }

    public int getEvTxtId() {
        return evTxtId;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public void setEvTxtId(int evTxtId) {
        this.evTxtId = evTxtId;
    }

    public double getEvPrecision() {
        return evPrecision;
    }

    public void setEvPrecision(double evPrecision) {
        this.evPrecision = evPrecision;
    }

    public double getEvRecall() {
        return evRecall;
    }

    public void setEvRecall(double evRecall) {
        this.evRecall = evRecall;
    }

    public double getEvFMeasure() {
        return evFMeasure;
    }

    public void setEvFMeasure(double evFMeasure) {
        this.evFMeasure = evFMeasure;
    }

    //**************************************************************************
    public Evaluation() {
    }

    public Evaluation(int evTxtId, double evPrecision, double evRecall, double evFMeasure) {
        this.evTxtId = evTxtId;
        this.evPrecision = evPrecision;
        this.evRecall = evRecall;
        this.evFMeasure = evFMeasure;
    }
}