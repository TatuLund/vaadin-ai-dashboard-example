/*
 * Copyright 2000-2026 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.ai.provider.DatabaseProvider;

/**
 * In-memory H2 database implementation of DatabaseProvider for testing and demo
 * purposes.
 *
 * @author Vaadin Ltd
 */
public class InMemoryDatabaseProvider implements DatabaseProvider {

    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public InMemoryDatabaseProvider() {
        DemoDataInitializer.initialize(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public String getSchema() {
        var schema = new StringBuilder("DATABASE SCHEMA:\n\n");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (Statement stmt = conn.createStatement();
                // H2-specific SQL command that exports the database schema as a series of DDL statements (requires admin)
                var rs = stmt.executeQuery("SCRIPT NODATA NOSETTINGS")) {
                while (rs.next()) {
                    var line = rs.getString(1);
                    if (line.startsWith("CREATE TABLE") || line.startsWith("CREATE MEMORY TABLE")) {
                        schema.append(line).append("\n\n");
                    }
                }
            }

            schema.append("""
                NOTES:
                - This is an H2 database. Reserved words like MONTH, VALUE, etc. must be quoted with double quotes when used as identifiers
                - Use "MONTH" with quotes when querying the sales table (reserved word)
                - value is a reserved alias
                - Do NOT use reserved words like VALUE, KEY, ORDER, etc. as column aliases. Use descriptive names instead (e.g. total_revenue, sale_count)
                - All tables support standard SQL SELECT queries
                - SALES table: use month_order column for chronological sorting (ORDER BY month_order)
                - WEBSITE_TRAFFIC table: both columns are 0-based indices. day_of_week: 0=Monday, 1=Tuesday, 2=Wednesday, 3=Thursday, 4=Friday. hour_of_day: 0=9am, 1=10am, ..., 7=4pm. Use xAxis/yAxis categories in configuration to set the display labels.
                """);

        } catch (SQLException e) {
            return "Error retrieving database schema: " + e.getMessage();
        }

        return schema.toString();
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql) {
        try (var conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            var stmt = conn.prepareStatement(sql);
            var rs = stmt.executeQuery()) {
            // Convert ResultSet to List of Maps (one map per row, column names as keys)
            var meta = rs.getMetaData();
            var rows = new ArrayList<Map<String, Object>>();
            while (rs.next()) {
                var row = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnLabel(i), rs.getObject(i));
                }
                rows.add(row);
            }
            return rows;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Query failed: " + e.getMessage(), e);
        }
    }
}
