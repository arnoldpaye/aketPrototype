package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.Keyword;
import com.apt.aket.model.Text;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
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

    public void insertItem(DataStore dataStore, Keyword keyword) throws SQLException {
        try {
            dataStore.execute(new StatementReader("sql/keyword.xml").getStatement("insertKeyword"), keyword);
        } catch (SQLException sqle) {
            throw sqle;
        }
    }
    public void updateItem(DataStore dataStore, Keyword keyword) throws SQLException {
        try {
            dataStore.execute(new StatementReader("sql/keyword.xml").getStatement("updateKeyword"), keyword);
        } catch (SQLException sqle) {
            throw sqle;
        }
    }
}