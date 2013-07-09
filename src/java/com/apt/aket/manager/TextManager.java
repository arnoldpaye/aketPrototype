package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.Text;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.DSDescriptor;
import org.openlogics.cjb.jsf.controller.DefaultManager;

/**
 * Project: aketPrototype Package: com.apt.aket.manager Class : TextManager.java
 * (UTF-8)
 *
 * @date 05/07/2013
 * @author Arnold Paye
 */

@ManagedBean
@ViewScoped
@DSDescriptor("sql/text.xml")
public class TextManager extends DefaultManager<Text> {
    
    @Override
    protected List<Text> fetchDataFromDataSource() throws SQLException, FileNotFoundException, IOException {
        DataStore dataStore = DataStoreManager.getDataStore();
        data.clear();
        dataStore.select(getStatementReader().getStatement("getTexts"), Text.class, new MappedResultVisitor<Text>() {
            @Override
            public void visit(Text text, DataStore dataStore, ResultSet resultSet) throws SQLException {
                data.add(text);  
            }
        });
        return data;
        
    }
    
    @Override
    public void selectionFeaturePerformed() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
