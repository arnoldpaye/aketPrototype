package com.apt.aket.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.JdbcDataStore;

/**
 * Project: aketPrototype 
 * Package: com.apt.aket.data 
 * Class : DataStoreManager.java (UTF-8)
 *
 * @date 09/07/2013
 * @author Arnold Paye
 */
public class DataStoreManager {

    public static DataStore getDataStore() throws FileNotFoundException, IOException {
        InputStream stream = null;
        try {
            File configDir = new File(System.getProperty("catalina.base"), "conf");
            File configFile = new File(configDir, "aket.properties");
            stream = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(stream);
            return new JdbcDataStore(properties);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch(IOException ioe) {
                    
                }
            }
        }
    }
}
