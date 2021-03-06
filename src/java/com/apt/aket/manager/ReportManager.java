package com.apt.aket.manager;

import com.apt.aket.model.Evaluation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 * @project aketPrototype
 * @package com.apt.aket.manager
 * @class ReportManager.java (UTF-8)
 * @date 31/07/2013
 * @author Arnold Paye
 */
@ManagedBean
@SessionScoped
public class ReportManager {

    /* Members */
    private int careerId;
    private List<Evaluation> evaluations = new ArrayList<Evaluation>();
    private CartesianChartModel precisionModel;
    private CartesianChartModel recallModel;
    private CartesianChartModel fmeasureModel;
    LineChartSeries seriesp1 = new LineChartSeries();
    LineChartSeries seriesp2 = new LineChartSeries();
    LineChartSeries seriesr1 = new LineChartSeries();
    LineChartSeries seriesr2 = new LineChartSeries();
    LineChartSeries seriesf1 = new LineChartSeries();
    LineChartSeries seriesf2 = new LineChartSeries();

    /* Getters and setters */
    public int getCareerId() {
        return careerId;
    }

    public void setCareerId(int careerId) {
        this.careerId = careerId;
    }

    public CartesianChartModel getPrecisionModel() {
        return precisionModel;
    }

    public CartesianChartModel getRecallModel() {
        return recallModel;
    }

    public CartesianChartModel getFmeasureModel() {
        return fmeasureModel;
    }

    public ReportManager() throws FileNotFoundException, SQLException, IOException {
        loadEvaluations();

    }

    public void loadEvaluations() throws FileNotFoundException, SQLException, IOException {
        try {
            EvaluationManager evaluationManager = new EvaluationManager();
            evaluationManager.setCareerId(careerId);
            evaluations = evaluationManager.getEvaluations();
            if (evaluations.size() > 0) {
                
                seriesp1 = new LineChartSeries();
                seriesr1 = new LineChartSeries();
                seriesf1 = new LineChartSeries();
                seriesp2 = new LineChartSeries();
                seriesr2 = new LineChartSeries();
                seriesf2 = new LineChartSeries();
                int i = 1;
                for (Evaluation evaluation : evaluations) {
                    if ("KOHA".equals(evaluation.getSource())) {
                        seriesp1.set(i, evaluation.getEvPrecision());
                        seriesr1.set(i, evaluation.getEvRecall());
                        seriesf1.set(i, evaluation.getEvFMeasure());
                    } else if ("TEXTRANK".equals(evaluation.getSource())) {
                        seriesp2.set(i, evaluation.getEvPrecision());
                        seriesr2.set(i, evaluation.getEvRecall());
                        seriesf2.set(i, evaluation.getEvFMeasure());
                        i++;
                    }
                }
                createPrecisionModel();
                createRecallModel();
                createFMeasureModel();
            } else {
                precisionModel = null;
                recallModel = null;
                fmeasureModel = null;
            }
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        } catch (IOException ioe) {
            throw ioe;
        } catch (SQLException sqle) {
            throw sqle;
        }
    }

    private void createPrecisionModel() throws SQLException, FileNotFoundException, IOException {
        precisionModel = new CartesianChartModel();
        seriesp1.setLabel("KOHA");
        seriesp2.setLabel("TEXTRANK");
        precisionModel.addSeries(seriesp1);
        precisionModel.addSeries(seriesp2);
    }

    private void createRecallModel() {
        recallModel = new CartesianChartModel();
        seriesr1.setLabel("KOHA");
        seriesr2.setLabel("TEXTRANK");
        recallModel.addSeries(seriesr1);
        recallModel.addSeries(seriesr2);
    }

    private void createFMeasureModel() {
        fmeasureModel = new CartesianChartModel();
        seriesf1.setLabel("KOHA");
        seriesf2.setLabel("TEXTRANK");
        fmeasureModel.addSeries(seriesf1);
        fmeasureModel.addSeries(seriesf2);
    }
}