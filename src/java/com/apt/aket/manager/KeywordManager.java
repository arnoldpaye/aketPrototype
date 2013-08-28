package com.apt.aket.manager;

import com.apt.aket.model.Keyword;
import com.apt.aket.model.Text;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.StatementReader;

/**
 * @project aketPrototype
 * @package com.apt.aket.manager
 * @class KeywordManager.java (UTF-8)
 * @date 09/08/2013
 * @author Arnold Paye
 */
public class KeywordManager {

    /**
     * Get all keywords from a selected text.
     * @param dataStore
     * @param text
     * @return
     * @throws SQLException 
     */
    public List<Keyword> getKeywords(DataStore dataStore, Text text) throws SQLException {
        final List<Keyword> keywordList = new ArrayList<Keyword>();
        try {
            dataStore.select(new StatementReader("sql/keyword.xml").getStatement("getKeyword"), text, Keyword.class, new MappedResultVisitor<Keyword>() {
                @Override
                public void visit(Keyword keyword, DataStore dataStore, ResultSet resultSet) {
                    keywordList.add(keyword);
                }
            });
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            return keywordList;
        }
    }

    /**
     * Insert keyword item into database.
     * @param dataStore
     * @param keyword
     * @throws SQLException 
     */
    public void insertItem(DataStore dataStore, Keyword keyword) throws SQLException {
        try {
            dataStore.execute(new StatementReader("sql/keyword.xml").getStatement("insertKeyword"), keyword);
        } catch (SQLException sqle) {
            throw sqle;
        }
    }
    
    /**
     * Update keyword item in database.
     * @param dataStore
     * @param keyword
     * @throws SQLException 
     */
    public void updateItem(DataStore dataStore, Keyword keyword) throws SQLException {
        try {
            dataStore.execute(new StatementReader("sql/keyword.xml").getStatement("updateKeyword"), keyword);
        } catch (SQLException sqle) {
            throw sqle;
        }
    }
}