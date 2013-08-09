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
    @Column("ev_kw_id")
    private int evKwId;
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
    
    
}