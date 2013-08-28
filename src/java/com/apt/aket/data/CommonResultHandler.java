package com.apt.aket.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.openlogics.cjb.jdbc.DataStore;
import org.openlogics.cjb.jdbc.ResultVisitor;

/**
 * @project aketPrototype
 * @package com.apt.aket.data
 * @class CommonResultHandler.java (UTF-8)
 * @date 09/08/2013
 * @author Arnold Paye
 */
public class CommonResultHandler {

    /**
     * Implement a integer handler.
     */
    public static class IntegerHandler implements ResultVisitor<Integer> {

        @Override
        public Integer visit(ResultSet rs, DataStore dataSource) throws SQLException {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
}