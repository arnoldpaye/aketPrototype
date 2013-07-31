package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import static com.apt.aket.manager.TextManager.log;
import com.apt.aket.model.Evaluation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.apache.commons.math.util.MathUtils;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.DSDescriptor;
import org.openlogics.cjb.jsf.controller.DefaultManager;

/**
 * @project aketPrototype
 * @package com.apt.aket.manager
 * @class EvaluationManager.java (UTF-8)
 * @date 30/07/2013
 * @author Arnold Paye
 */
@ManagedBean
@SessionScoped
@DSDescriptor("sql/evaluation.xml")
public class EvaluationManager extends DefaultManager<Evaluation> {

    private SummaryStatistics ssPrecision;
    private SummaryStatistics ssRecall;
    private SummaryStatistics ssFMeasure;

    @Override
    protected List<Evaluation> fetchDataFromDataSource() throws Exception {
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            ssPrecision = new SummaryStatistics();
            ssRecall = new SummaryStatistics();
            ssFMeasure = new SummaryStatistics();
            data.clear();
            dataStore.select(getStatementReader().getStatement("getAllEvaluations"), Evaluation.class, new MappedResultVisitor<Evaluation>() {
                @Override
                public void visit(Evaluation evaluation, DataStore dataStore, ResultSet resultSet) {
                    data.add(evaluation);
                    ssPrecision.addValue(evaluation.getEvPrecision());
                    ssRecall.addValue(evaluation.getEvRecall());
                    ssFMeasure.addValue(evaluation.getEvFMeasure());
                }
            });
        } catch (SQLException sqle) {
            log.error("SQLException in fetchDataFromDataSource method->" + sqle.getMessage());
        } catch (IOException ioe) {
            log.error("IOException in fetchDataFromDataSource method->" + ioe.getMessage());
        } finally {
            return data;
        }
    }

    @Override
    public void selectionFeaturePerformed() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void insertItem(Evaluation evaluation) throws FileNotFoundException, IOException, SQLException {
        DataStore dataStore = DataStoreManager.getDataStore();
        dataStore.setAutoCommit(false);
        // The last evaluation is deleted
        dataStore.execute(getStatementReader().getStatement("deleteLastEvaluation"), evaluation);
        dataStore.execute(getStatementReader().getStatement("insertEvaluation"), evaluation);
        dataStore.commit();
    }

    public void deleteSelected() throws FileNotFoundException, IOException, SQLException {
        DataStore dataStore = DataStoreManager.getDataStore();
        dataStore.setAutoCommit(false);
        try {
            dataStore.execute(getStatementReader().getStatement("deleteEvaluation"), selected);
            dataStore.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Evaluacion eliminada."));
        } catch (SQLException sqle) {
            dataStore.rollBack();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null, sqle.getMessage()));
        } finally {
            refresh();
        }
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
}
