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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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

    /* Getters and Setters */
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

    public void deleteEvaluation(Keyword keyword) throws FileNotFoundException, IOException, SQLException {
        DataStore dataStore = DataStoreManager.getDataStore();
        dataStore.setAutoCommit(false);
        dataStore.execute(new StatementReader("sql/evaluation.xml").getStatement("deleteEvaluation"), keyword);
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
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.select(new StatementReader("sql/evaluation.xml").getStatement("getEvaluations"), Evaluation.class, new MappedResultVisitor<Evaluation>() {
                @Override
                public void visit(Evaluation evaluation, DataStore ds, ResultSet rs) throws SQLException {
                    KeywordManager keywordManager = new KeywordManager();
                    Keyword keyword = keywordManager.getKeyword(ds, evaluation);
                    if (keyword.getKwSource() == 1) {
                        evaluation.setSource("KOHA");
                    } else if (keyword.getKwSource() == 0) {
                        evaluation.setSource("TEXTRANK");
                    }
                    evaluationList.add(evaluation);
                }
            });
            System.out.println("DBG " + evaluationList.size());
        } catch (IOException ioe) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, ioe.getMessage()));
        } catch (SQLException sqle) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, sqle.getMessage()));

        } finally {
            return evaluationList;
        }
    }
}