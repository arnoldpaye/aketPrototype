package com.apt.aket.data;

import java.util.Properties;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.JdbcDataStore;

/**
 * Project: aketPrototype Package: com.apt.aket.data Class :
 * DataStoreManager.java (UTF-8)
 *
 * @date 09/07/2013
 * @author Arnold Paye
 */
public class DataStoreManager {

    public static DataStore getDataStore() {
        Properties properties = new Properties();
        //TODO: load properties data from a file in the server
        properties.setProperty("driver", "org.postgresql.Driver");
        properties.setProperty("url", "jdbc:postgresql://localhost:5432/aket_db");
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "postgres");
        try {
            return new JdbcDataStore(properties);
        } finally {
        }
    }
}
