package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.WordSelection;
import com.apt.aket.model.WordTag;
import com.apt.textrank.Util;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.DSDescriptor;
import org.openlogics.cjb.jee.util.JEEContext;
import org.openlogics.cjb.jsf.controller.DefaultManager;

/**
 * @project aketPrototype
 * @package com.apt.aket.manager
 * @class WordTagManager.java (UTF-8)
 * @date 20/07/2013
 * @author Arnold Paye
 */
@ManagedBean
@SessionScoped
@DSDescriptor("sql/word_tag.xml")
public class WordTagManager extends DefaultManager<WordTag> {

    static Logger log = Logger.getLogger(WordTagManager.class);
    private List<WordTag> wordTags;
    private List<WordSelection> wordSelections;
    private double[][] adjacencyMatrix;

    public List<WordSelection> getWordSelections() {
        return wordSelections;
    }

    public double[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    @Override
    protected List<WordTag> fetchDataFromDataSource() throws Exception {
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            data.clear();
            log.info("DataStore select" + getStatementReader().getStatement("getAllWordTags"));
            dataStore.select(getStatementReader().getStatement("getAllWordTags"), WordTag.class, new MappedResultVisitor<WordTag>() {
                @Override
                public void visit(WordTag wordTag, DataStore dataStore, ResultSet resultSet) throws SQLException {
                    data.add(wordTag);
                }
            });
        } catch (SQLException sqle) {
            log.error("SQLException " + sqle.getMessage());
        } catch (IOException ioe) {
            log.error("IOException " + ioe.getMessage());
        } finally {
            return data;
        }
    }

    @Override
    public void selectionFeaturePerformed() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<WordTag> getWordTags() {
        wordTags = new ArrayList<WordTag>();
        TextManager textManager = JEEContext.getSessionScopedBean(TextManager.class);
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.select(getStatementReader().getStatement("getWordTagsByText"), textManager.getSelected().getTxtId(), WordTag.class, new MappedResultVisitor<WordTag>() {
                @Override
                public void visit(WordTag wordTag, DataStore ds, ResultSet rs) throws SQLException {
                    wordTags.add(wordTag);
                }
            });
        } catch (SQLException sqle) {
            log.error("SQLException " + sqle.getMessage());
        } catch (IOException ioe) {
            log.error("IOException " + ioe.getMessage());
        } finally {
            return wordTags;
        }
    }

    public void prepareWordSelection() {
        wordSelections = Util.filterPOS(wordTags);
        for (int i = 0; i < wordSelections.size(); i++) {
            System.out.println(i + " " + wordSelections.get(i).getValue());
        }
        adjacencyMatrix = Util.buildAdjacencyMatrix(wordSelections);
    }

    public void initSelection() {
        if (wordSelections == null || wordSelections.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Debe obtener la lista de palabras primero."));
        } else {
            double[] pr = Util.pageRank(adjacencyMatrix);
            for (int i = 0; i < pr.length; i++) {
                wordSelections.get(i).setRank(pr[i]);
            }
            Collections.sort(wordSelections);
        }

    }

    public String cancelSelectedSelection() {
        wordTags = null;
        wordSelections = null;
        return "/index?faces-redirect=true";
    }
}