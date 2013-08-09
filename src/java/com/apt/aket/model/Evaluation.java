package com.apt.aket.model;

import org.apache.commons.math.util.MathUtils;
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
    @Column("ev_kw_id")
    private int evKwId;
    @Column("txt_title")
    private String txtTitle;
    @Column("ev_precision")
    private double evPrecision;
    @Column("ev_recall")
    private double evRecall;
    @Column("ev_fmeasure")
    private double evFMeasure;

    public Evaluation() {
    }

    public Evaluation(int evKwId, double evPrecision, double evRecall, double evFMeasure) {
        this.evKwId = evKwId;
        this.evPrecision = evPrecision;
        this.evRecall = evRecall;
        this.evFMeasure = evFMeasure;
    }

    public int getEvId() {
        return evId;
    }

    public void setEvId(int evId) {
        this.evId = evId;
    }

    public int getEvKwId() {
        return evKwId;
    }

    public void setEvKwId(int evKwId) {
        this.evKwId = evKwId;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public double getEvPrecision() {
        return MathUtils.round(evPrecision, 2);
    }

    public void setEvPrecision(double evPrecision) {
        this.evPrecision = evPrecision;
    }

    public double getEvRecall() {
        return MathUtils.round(evRecall, 2);
    }

    public void setEvRecall(double evRecall) {
        this.evRecall = evRecall;
    }

    public double getEvFMeasure() {
        return MathUtils.round(evFMeasure, 2);
    }

    public void setEvFMeasure(double evFMeasure) {
        this.evFMeasure = evFMeasure;
    }
}