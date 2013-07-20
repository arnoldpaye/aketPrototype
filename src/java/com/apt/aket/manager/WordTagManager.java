package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.Text;
import com.apt.aket.model.WordTag;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private List<WordTag> words;

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

    public List<WordTag> getWords() {
        words = new ArrayList<WordTag>();
        TextManager textManager = JEEContext.getSessionScopedBean(TextManager.class);
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.select(getStatementReader().getStatement("getWordTagsByText"), textManager.getSelected().getTxtId(), WordTag.class, new MappedResultVisitor<WordTag>() {
                @Override
                public void visit(WordTag wordTag, DataStore ds, ResultSet rs) throws SQLException {
                    words.add(wordTag);
                }
            });
        } catch (SQLException sqle) {
            log.error("SQLException " + sqle.getMessage());
        } catch (IOException ioe) {
            log.error("IOException " + ioe.getMessage());
        } finally {
            return words;
        }
    }
}
