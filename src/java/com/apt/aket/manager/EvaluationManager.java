package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.Evaluation;
import com.apt.aket.model.Keyword;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.commons.math.util.MathUtils;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.StatementReader;

/**
 * @project aketPrototype
 * @package com.apt.aket.manager
 * @class EvaluationManager.java (UTF-8)
 * @date 30/07/2013
 * @author Arnold Paye
 */
@ManagedBean
@SessionScoped
public class EvaluationManager {

    /* Members */
    private int keywordSource = 1;
    private SummaryStatistics ssPrecision;
    private SummaryStatistics ssRecall;
    private SummaryStatistics ssFMeasure;

    /* Getters and Setters */
    public int getKeywordSource() {
        return keywordSource;
    }

    public void setKeywordSource(int keywordSource) {
        this.keywordSource = keywordSource;
    }

    public double getPrecisionMean() {
        return MathUtils.round(ssPrecision.getMean(), 2);
    }

    public double getRecallMean() {
        return MathUtils.round(ssRecall.getMean(), 2);
    }

    public double getFMeasureMean() {
        return MathUtils.round(ssFMeasure.getMean(), 2);
    }

    /**
     * Default constructor.
     *
     * @throws FileNotFoundException
     */
    public EvaluationManager() throws FileNotFoundException {
        loadEvaluations();
    }

    /**
     * Insert evaluation item into database.
     *
     * @param evaluation
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public void insertItem(Evaluation evaluation) throws FileNotFoundException, IOException, SQLException {
        DataStore dataStore = DataStoreManager.getDataStore();
        dataStore.setAutoCommit(false);
        // The last evaluation is deleted
        dataStore.execute(new StatementReader("sql/evaluation.xml").getStatement("deleteLastEvaluation"), evaluation);
        dataStore.execute(new StatementReader("sql/evaluation.xml").getStatement("insertEvaluation"), evaluation);
        dataStore.commit();
    }

    /**
     * Load all evaluations.
     *
     * @throws FileNotFoundException
     */
    public void loadEvaluations() throws FileNotFoundException {
        getEvaluations();
    }

    /**
     * Function for loadEvaluations.
     *
     * @return
     * @throws FileNotFoundException
     */
    public List<Evaluation> getEvaluations() throws FileNotFoundException {
        final List<Evaluation> evaluationList = new ArrayList<Evaluation>();
        Keyword keyword = new Keyword();
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            ssPrecision = new SummaryStatistics();
            ssRecall = new SummaryStatistics();
            ssFMeasure = new SummaryStatistics();
            dataStore.select(new StatementReader("sql/evaluation.xml").getStatement("getEvaluations"), keywordSource, Evaluation.class, new MappedResultVisitor<Evaluation>() {
                @Override
                public void visit(Evaluation evaluation, DataStore ds, ResultSet rs) throws SQLException {
                    evaluationList.add(evaluation);
                    ssPrecision.addValue(evaluation.getEvPrecision());
                    ssRecall.addValue(evaluation.getEvRecall());
                    ssFMeasure.addValue(evaluation.getEvFMeasure());
                }
            });
        } finally {
            return evaluationList;
        }
    }
}
