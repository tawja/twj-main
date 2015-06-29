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
package org.tawja.platform.plugins.database.jdbc.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Random;

/**
 *
 * @author jbennani
 */
public class SqlUtilsForTests {

    private static Random random = new Random();

    public static void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        System.out.println("   - Memory : Free : " + format.format(freeMemory / 1024));
        System.out.println("   - Memory : Allocated : " + format.format(allocatedMemory / 1024));
        System.out.println("   - Memory : Max : " + format.format(maxMemory / 1024));
        System.out.println("   - Memory  : Total Free : " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
    }

    private static void printUsage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("get")
                    && Modifier.isPublic(method.getModifiers())) {
                Object value;
                try {
                    value = method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = e;
                } // try
                System.out.println(method.getName() + " = " + value);
            } // if
        } // for
    }

    public static void initDb2(Connection conn, int noOfThreads) throws SQLException {
        execSqlNoErr(conn, "drop table temptest");
        execSql(conn, "create table temptest (threadNo integer, ctr integer)");
        for (int i = 0; i < noOfThreads; i++) {
            execSql(conn, "insert into temptest values(" + i + ",0)");
        }
    }

    public static void incrementThreadCounter(Connection conn, int threadNo) throws SQLException {
        execSql(conn, "update temptest set ctr = ctr + 1 where threadNo=" + threadNo);
    }

    public static void randomThreadCounter(Connection conn) throws SQLException {
        incrementThreadCounter(conn, getRandom(1, 10));
    }

    public static int getRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static void execSqlAndPrintThreadCounter(Connection conn) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select threadNo, ctr from temptest");
            System.out.println("+ SQL results : (thread counter) : ");
            while (rs.next()) {
                System.out.println("     - threadNo = '" + rs.getInt(1) + "'; ctr = '" + rs.getInt(1) + "'.");
            }
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static void execSqlAndPrintThreadCounterSilent(Connection conn) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery("select threadNo, ctr from temptest");
            //System.out.println("+ SQL results : (thread counter) : ");
            while (rs.next()) {
                //System.out.println("     - threadNo = '" + rs.getInt(1) + "'; ctr = '" + rs.getInt(1) + "'.");
            }
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public static void execSqlNoErr(Connection conn, String sql) {
        try {
            execSql(conn, sql);
        } catch (SQLException e) {
        }
    }

    public static void execSql(Connection conn, String sql) throws SQLException {
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
