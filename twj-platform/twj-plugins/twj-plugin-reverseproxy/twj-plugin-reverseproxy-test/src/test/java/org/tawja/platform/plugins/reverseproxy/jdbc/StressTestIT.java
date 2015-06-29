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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
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
public class StressTestIT {

    public StressTestIT() {
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
    private static final int maxConnections = 8;     // number of connections
    private static final int noOfThreads = 50;    // number of worker threads
    private static final int processingTime = 30;    // total processing time of the test program in seconds
    private static final int threadPauseTime1 = 100;   // max. thread pause time in microseconds, without a connection
    private static final int threadPauseTime2 = 100;   // max. thread pause time in microseconds, with a connection

    private static MiniConnectionPoolManager poolMgr;
    private static WorkerThread[] threads;
    private static boolean shutdownFlag;
    private static Object shutdownObj = new Object();
    private static Random random = new Random();

    private static class WorkerThread extends Thread {

        public int threadNo;

        public void run() {
            threadMain(threadNo);
        }
    };

    @Test
    public void StressTest() throws Exception {
        System.out.println("Program started.");
        ConnectionPoolDataSource dataSource = DataSourceFactory.createDataSourceH2Mem();
        poolMgr = new MiniConnectionPoolManager(dataSource, maxConnections);
        initDb();
        startWorkerThreads();
        pause(processingTime * 1000000);
        System.out.println("\nStopping threads.");
        stopWorkerThreads();
        System.out.println("\nAll threads stopped.");
        poolMgr.dispose();
        System.out.println("Program completed.");
    }

    private static void startWorkerThreads() {
        threads = new WorkerThread[noOfThreads];
        for (int threadNo = 0; threadNo < noOfThreads; threadNo++) {
            WorkerThread thread = new WorkerThread();
            threads[threadNo] = thread;
            thread.threadNo = threadNo;
            thread.start();
        }
    }

    private static void stopWorkerThreads() throws Exception {
        setShutdownFlag();
        for (int threadNo = 0; threadNo < noOfThreads; threadNo++) {
            threads[threadNo].join();
        }
    }

    private static void setShutdownFlag() {
        synchronized (shutdownObj) {
            shutdownFlag = true;
            shutdownObj.notifyAll();
        }
    }

    private static void threadMain(int threadNo) {
        try {
            threadMain2(threadNo);
        } catch (Throwable e) {
            System.out.println("\nException in thread " + threadNo + ": " + e);
            e.printStackTrace(System.out);
            setShutdownFlag();
        }
    }

    private static void threadMain2(int threadNo) throws Exception {
        // System.out.println("Thread "+threadNo+" started.");
        while (true) {
            if (!pauseRandom(threadPauseTime1)) {
                return;
            }
            threadTask(threadNo);
        }
    }

    private static void threadTask(int threadNo) throws Exception {
        Connection conn = null;
        try {
            conn = poolMgr.getConnection();
            if (shutdownFlag) {
                return;
            }
            System.out.print(threadNo + " ");
            incrementThreadCounter(conn, threadNo);
            pauseRandom(threadPauseTime2);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private static boolean pauseRandom(int maxPauseTime) throws Exception {
        return pause(random.nextInt(maxPauseTime));
    }

    private static boolean pause(int pauseTime) throws Exception {
        synchronized (shutdownObj) {
            if (shutdownFlag) {
                return false;
            }
            if (pauseTime <= 0) {
                return true;
            }
            int ms = pauseTime / 1000;
            int ns = (pauseTime % 1000) * 1000;
            shutdownObj.wait(ms, ns);
        }
        return true;
    }

    private static void initDb() throws SQLException {
        Connection conn = null;
        try {
            conn = poolMgr.getConnection();
            System.out.println("initDb connected");
            initDb2(conn);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        System.out.println("initDb done");
    }

    private static void initDb2(Connection conn) throws SQLException {
        execSqlNoErr(conn, "drop table temp");
        execSql(conn, "create table temp (threadNo integer, ctr integer)");
        for (int i = 0; i < noOfThreads; i++) {
            execSql(conn, "insert into temp values(" + i + ",0)");
        }
    }

    private static void incrementThreadCounter(Connection conn, int threadNo) throws SQLException {
        execSql(conn, "update temp set ctr = ctr + 1 where threadNo=" + threadNo);
    }

    private static void execSqlNoErr(Connection conn, String sql) {
        try {
            execSql(conn, sql);
        } catch (SQLException e) {
        }
    }

    private static void execSql(Connection conn, String sql) throws SQLException {
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeUpdate(sql);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
}
