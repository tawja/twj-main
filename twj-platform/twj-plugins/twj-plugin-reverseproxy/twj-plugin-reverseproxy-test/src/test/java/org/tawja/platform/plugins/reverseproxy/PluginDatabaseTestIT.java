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
package org.tawja.platform.plugins.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Map;
import java.util.Properties;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.apache.felix.ipojo.ComponentInstance;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.service.jdbc.DataSourceFactory;
import org.ow2.chameleon.testing.helpers.IPOJOHelper;
import org.tawja.platform.api.core.Hello;
import org.tawja.platform.plugins.database.api.TwjDatabaseFactory;
import org.tawja.platform.plugins.database.api.TwjDatabaseServer;
import org.tawja.platform.plugins.database.api.TwjDatabaseService;
import org.tawja.platform.plugins.database.hsql.HsqldbServiceProperties;
import org.tawja.platform.plugins.database.hsql.impl.HsqldbDatabaseServer;
import org.tawja.platform.tests.utils.TestBase;

/**
 * Tests the given bundle with JUnit and Pax Exam.
 *
 * @author Jaafar BENNANI-SMIRES
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class PluginDatabaseTestIT extends TestBase {

    /**
     * Logger.
     */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(PluginDatabaseTestIT.class);
    private IPOJOHelper ipojoHelper;

    @Inject
    BundleContext context;

    @Inject
    private org.apache.felix.ipojo.Factory ipojoFactory;

    @Inject
    private Hello helloService;

    @Inject
    private TwjDatabaseService twjDatabaseService;

    @Inject
    @Filter("(osgi.jdbc.driver.class=org.h2.Driver)")
    private DataSourceFactory dsfH2;

    public void setUp() {
        ipojoHelper = new IPOJOHelper(context);
    }

    public void tearDown() {
        ipojoHelper.dispose();
    }

    @Configuration
    public Option[] config() {

        // Reduce log level.
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        return options(
                cleanCaches(),
                ipojoBundles(),
                twjBundles(),
                junitBundles(),
                mavenBundle("org.ops4j.pax.jdbc", "pax-jdbc-spec").versionAsInProject(),
                mavenBundle("org.ops4j.pax.jdbc", "pax-jdbc").versionAsInProject(),
                mavenBundle("com.h2database", "h2").versionAsInProject(),
                mavenBundle("org.hsqldb", "hsqldb").versionAsInProject(),
                mavenBundle("org.tawja.platform.plugins", "twj-plugin-database-api").versionAsInProject(),
                mavenBundle("org.tawja.platform.plugins", "twj-plugin-database-base").versionAsInProject(),
                mavenBundle("org.tawja.platform.plugins", "twj-plugin-database-impl-hsql").versionAsInProject(),
                mavenBundle("org.tawja.platform.plugins", "twj-plugin-database-impl-h2").versionAsInProject(),
                mavenBundle("org.tawja.platform.plugins", "twj-plugin-database-commands").versionAsInProject()
        );
    }

    @Test
    public void checkInject() {
        assertNotNull("Null Bundle Context", context);
        assertNotNull("Null Hello Service", helloService);
    }

    @Test
    public void checkHelloBundle() {
        Boolean bundleHelloFound = false;
        Boolean bundleHelloActive = false;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle != null) {
                if ("twj-api".equals(bundle.getSymbolicName())) {
                    bundleHelloFound = true;
                    if (bundle.getState() == Bundle.ACTIVE) {
                        bundleHelloActive = true;
                    }
                }
            }
        }
        assertTrue(bundleHelloFound);
        assertTrue(bundleHelloActive);
    }

    @Test
    public void getHelloService() {
        assertEquals("Hello test", helloService.sayHello("test"));
    }

    @Test
    public void printTwjDatabaseServiceFactories() {
        Map<String, TwjDatabaseFactory> dbFactories = twjDatabaseService.getFactories();
        LOG.info("+ TwjDatabaseService Factories : ");
        for (TwjDatabaseFactory factory : dbFactories.values()) {
            LOG.info("   -  TwjDatabaseService Factory : " + factory.getName() + " (" + factory.getType() + ")");
        }
    }

    @Test
    public void getTwjDatabaseService() {
        Map<String, TwjDatabaseFactory> dbFactories = twjDatabaseService.getFactories();
        assertNotNull(twjDatabaseService);
        //assertNotNull("TwjDatabaseService test", dbFactories.get(HsqldbServiceProperties.SERVER_TYPE_NAME));
    }

    @Test
    public void startHsqlDbServer() {
        //Map<String, TwjDatabaseFactory> dbFactories = twjDatabaseService.getFactories();
        //TwjDatabaseFactory hsqlDbFactory = dbFactories.get(HsqldbServiceProperties.SERVER_TYPE_NAME);
        //assertNotNull("HsqlDb factory not available", dbFactories.get(HsqldbServiceProperties.SERVER_TYPE_NAME));

        // Default port to 9001, s, we start at 9101
        //TwjDatabaseServer server = twjDatabaseService.createtServer(HsqldbServiceProperties.SERVER_TYPE_NAME);
        Properties config = new Properties();
        config.put("name", "default");
        config.put("type", "hsql");
        config.put("host", "localhost");
        config.put("port", "9010");
        ComponentInstance instance = ipojoHelper.createComponentInstance("org.tawja.platform.plugins.database.hsql.server", config);;
        
        TwjDatabaseServer server = ipojoHelper.getServiceObjectByName(TwjDatabaseServer.class, instance.getInstanceName());
        
        server.start();

        LOG.info("Database Server" + server.getName() + ", Status : " + server.getStatus());
    }

    @Test
    public void createH2DataSourceAndConnection() throws SQLException {
        assertNotNull(dsfH2);
        Properties props = new Properties();
        //props.setProperty(DataSourceFactory.JDBC_URL, "jdbc:h2:mem:pax");
        //props.setProperty(DataSourceFactory.JDBC_URL, "jdbc:h2:tcp://localhost:9201/databases/test01");
        props.setProperty(DataSourceFactory.JDBC_URL, "jdbc:h2:file:./databases/h2/h2test.db");
        DataSource dataSource = dsfH2.createDataSource(props);
        assertNotNull(dataSource);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }

    @Test
    public void createH2Database() throws SQLException {
        //Map<String, TwjDatabaseFactory> dbFactories = twjDatabaseService.getFactories();
        //assertNotNull(twjDatabaseService);
        //assertNotNull("TwjDatabaseService test", dbFactories.get(HsqldbServiceProperties.SERVER_TYPE_NAME));

        
        Properties config = new Properties();
        config.put("name", "default");
        config.put("type", "h2");
        config.put("host", "localhost");
        config.put("port", "9020");
        ComponentInstance instance = ipojoHelper.createComponentInstance("org.tawja.platform.plugins.database.h2.server", config);;
        
        TwjDatabaseServer server = ipojoHelper.getServiceObjectByName(TwjDatabaseServer.class, instance.getInstanceName());
        
        server.start();

        LOG.info("Database Server" + server.getName() + ", Status : " + server.getStatus());
        
        
        //TwjDatabaseServer dbServer = twjDatabaseService.createtServer(HsqldbServiceProperties.SERVER_TYPE_NAME);
//
        //dbServer.setInnerServer(dbServer);
//
        //assertNotNull(dsfH2);
        //Properties props = new Properties();
        ////props.setProperty(DataSourceFactory.JDBC_URL, "jdbc:h2:mem:pax");
        ////props.setProperty(DataSourceFactory.JDBC_URL, "jdbc:h2:tcp://localhost:9201/databases/test01");
        //props.setProperty(DataSourceFactory.JDBC_URL, "jdbc:h2:file:./databases/h2/h2test.db");
        //DataSource dataSource = dsfH2.createDataSource(props);
        //assertNotNull(dataSource);
        //Connection connection = dataSource.getConnection();
        //assertNotNull(connection);
        //connection.close();
    }
}
