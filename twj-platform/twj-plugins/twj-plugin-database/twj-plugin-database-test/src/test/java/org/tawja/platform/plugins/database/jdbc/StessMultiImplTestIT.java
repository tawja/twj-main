/*
 * Copyright 2015 Tawja.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tawja.platform.plugins.database.jdbc;

import org.tawja.platform.plugins.database.jdbc.utils.DataSourceFactory;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.ConnectionPoolDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tawja.platform.plugins.database.jdbc.utils.SqlUtilsForTests;

/**
 *
 * @author jbennani
 */
public class StessMultiImplTestIT {

    public StessMultiImplTestIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void AllImplSimpleTest() throws Exception {
        System.out.println("Program started.");
        MiniConnectionPoolManager poolMgr;
        int measurementTime = 10000;

        // Add all database factories
        Map<String, ConnectionPoolDataSource> dsList = new LinkedHashMap<String, ConnectionPoolDataSource>();
        dsList.put("H2Mem", DataSourceFactory.createDataSourceH2Mem());
        dsList.put("H2File", DataSourceFactory.createDataSourceH2File());
        dsList.put("HsqlMem", DataSourceFactory.createDataSourceHsqlMem());
        dsList.put("HsqlFile", DataSourceFactory.createDataSourceHsqlFile());

        for (String dataSourceAlias : dsList.keySet()) {
            long startTime = System.currentTimeMillis();
            System.out.println("+ DataSource type : " + dataSourceAlias);
            ConnectionPoolDataSource dataSource = dsList.get(dataSourceAlias);
            poolMgr = new MiniConnectionPoolManager(dataSource, 10);
            Connection conn = poolMgr.getValidConnection();
            SqlUtilsForTests.initDb2(conn, 5);

            long cycles = 0;
            while (System.currentTimeMillis() - startTime < measurementTime) {
                //System.out.println("   - " + dataSourceAlias + " :  " + cycles);
                conn = poolMgr.getValidConnection();

                SqlUtilsForTests.randomThreadCounter(conn);
                SqlUtilsForTests.execSqlAndPrintThreadCounterSilent(conn);

                conn.close();
                cycles++;
            }

            poolMgr.dispose();
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("     - Total milliseconds: " + totalTime);
            System.out.println("     - Cycles: " + cycles);
            System.out.println("     - Microseconds per cycle: " + ((float) totalTime * 1000 / cycles));
            SqlUtilsForTests.printMemoryUsage();
        }

        System.out.println("Program completed.");
    }
}
