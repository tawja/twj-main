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
import javax.sql.ConnectionPoolDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.tawja.platform.plugins.database.jdbc.internal.MiniConnectionPoolManager;

/**
 *
 * @author jbennani
 */
public class SimpleTest {

    public SimpleTest() {
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
    public void SimpleTest() throws Exception {
        System.out.println("Program started.");
        MiniConnectionPoolManager poolMgr;
        ConnectionPoolDataSource dataSource = DataSourceFactory.createDataSourceH2Mem();
        poolMgr = new MiniConnectionPoolManager(dataSource, 10);
        Connection conn = poolMgr.getConnection();
        conn.close();
        poolMgr.dispose();
        System.out.println("Program completed.");
    }

    @Test
    public void DisconnectionTest() throws Exception {
        System.out.println("Program started.");
        MiniConnectionPoolManager poolMgr;
        ConnectionPoolDataSource dataSource = DataSourceFactory.createDataSourceH2Mem();
        poolMgr = new MiniConnectionPoolManager(dataSource, 5, 15, 15);
        testConnection(poolMgr);
        System.out.print("Restart the database server and press ENTER to continue - ");
        //System.console().readLine();
        Thread.sleep(30);
        testConnection(poolMgr);
        testConnection(poolMgr);
        poolMgr.dispose();
        System.out.println("Program completed.");
    }

    private static void testConnection(MiniConnectionPoolManager poolMgr) throws Exception {
        System.out.println("before getConnection(): " + getStatus(poolMgr));
        // Connection conn = poolMgr.getConnection();
        Connection conn = poolMgr.getValidConnection();
        System.out.println("after getConnection(): " + getStatus(poolMgr));
        System.out.println("connection.isValid()=" + conn.isValid(10));
        System.out.println("after isValid(): " + getStatus(poolMgr));
        conn.close();
        System.out.println("after close(): " + getStatus(poolMgr));
    }

    private static String getStatus(MiniConnectionPoolManager poolMgr) {
        return "activeConnections=" + poolMgr.getActiveConnections()
                + " inactiveConnections=" + poolMgr.getInactiveConnections();
    }

    @Test
    public void SpeedTest() throws Exception {
        MiniConnectionPoolManager poolMgr;
        int measurementTime = 10000;
        System.out.println("Program started.");
        ConnectionPoolDataSource dataSource = DataSourceFactory.createDataSourceH2Mem();
        poolMgr = new MiniConnectionPoolManager(dataSource, 10);
        long startTime = System.currentTimeMillis();
        long cycles = 0;
        while (System.currentTimeMillis() - startTime < measurementTime) {
            Connection conn = poolMgr.getConnection();
            conn.close();
            cycles++;
        }
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("Total milliseconds: " + totalTime);
        System.out.println("Cycles: " + cycles);
        System.out.println("Microseconds per cycle: " + ((float) totalTime * 1000 / cycles));
    }

}
