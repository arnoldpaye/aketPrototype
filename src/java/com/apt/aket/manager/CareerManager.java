package com.apt.aket.manager;

import com.apt.aket.data.DataStoreManager;
import com.apt.aket.model.Career;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.MappedResultVisitor;
import org.openlogics.cjb.jee.jdbc.DSDescriptor;
import org.openlogics.cjb.jsf.controller.DefaultManager;

/**
 * @project aketPrototype
 * @package com.apt.aket.manager
 * @class CareerManager.java (UTF-8)
 * @date 26/09/2013
 * @author Arnold Paye
 */
@ManagedBean
@SessionScoped
@DSDescriptor("sql/career.xml")
public class CareerManager extends DefaultManager<Career> {

    @Override
    protected List<Career> fetchDataFromDataSource() {
        data.clear();
        try {
            DataStore dataStore = DataStoreManager.getDataStore();
            dataStore.select(getStatementReader().getStatement("getAllCareers"), Career.class, new MappedResultVisitor<Career>() {
                @Override
                public void visit(Career career, DataStore ds, ResultSet rs) throws SQLException {
                    data.add(career);
                }
            });
        } catch (SQLException sqle) {
            System.out.println("SQLException " + sqle.getMessage());
        } catch (IOException ioe) {
            System.out.println("IOException " + ioe.getMessage());
        }
        return data;
    }

    @Override
    public void selectionFeaturePerformed() throws Exception {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
