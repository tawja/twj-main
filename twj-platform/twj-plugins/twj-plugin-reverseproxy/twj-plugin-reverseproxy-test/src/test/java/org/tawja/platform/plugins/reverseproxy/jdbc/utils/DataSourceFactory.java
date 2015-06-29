// DataSource factory for the MiniConnectionPoolManager test programs.
package org.tawja.platform.plugins.database.jdbc.utils;

import java.io.PrintWriter;
import javax.sql.ConnectionPoolDataSource;

public class DataSourceFactory {

    public static ConnectionPoolDataSource createDataSourceH2Mem() throws Exception {
        org.h2.jdbcx.JdbcDataSource dataSource = new org.h2.jdbcx.JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testDbH2Mem;DB_CLOSE_DELAY=-1");    // in-memory database
        return dataSource;
    }

    public static ConnectionPoolDataSource createDataSourceH2File() throws Exception {
        org.h2.jdbcx.JdbcDataSource dataSource = new org.h2.jdbcx.JdbcDataSource();
        dataSource.setURL("jdbc:h2:file:./databases/h2/testDbFile.db");
        return dataSource;
    }

    public static ConnectionPoolDataSource createDataSourceHsqlMem() throws Exception {
        org.hsqldb.jdbc.pool.JDBCPooledDataSource dataSource = new org.hsqldb.jdbc.pool.JDBCPooledDataSource();
        dataSource.setUrl("jdbc:hsqldb:mem:testDbHsqlMem");    // in-memory database
        return dataSource;
    }

    public static ConnectionPoolDataSource createDataSourceHsqlFile() throws Exception {
        org.hsqldb.jdbc.pool.JDBCPooledDataSource dataSource = new org.hsqldb.jdbc.pool.JDBCPooledDataSource();
        // dataSource.setURL("jdbc:h2:file:c:/temp/tempTestMiniConnectionPoolManagerDb");
        dataSource.setUrl("jdbc:hsqldb:file:./databases/hsql/testDbFile");    // in-memory database
        return dataSource;
    }
}
