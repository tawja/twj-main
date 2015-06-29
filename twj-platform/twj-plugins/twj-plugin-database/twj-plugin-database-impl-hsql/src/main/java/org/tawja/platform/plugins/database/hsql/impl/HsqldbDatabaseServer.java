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
package org.tawja.platform.plugins.database.hsql.impl;

import java.util.Properties;
import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.ConfigurationException;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.MissingHandlerException;
import org.apache.felix.ipojo.UnacceptableConfiguration;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.hsqldb.server.Server;
import org.tawja.platform.plugins.database.base.AbstractTwjDatabaseServer;
import org.tawja.platform.plugins.database.api.TwjServerStatus;
import org.tawja.platform.plugins.database.hsql.HsqldbServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.database.base.TwjDatabaseUtils;

/**
 *
 * @author jbennani
 */
@Component(
        name = "org.tawja.platform.plugins.database.hsql.server",
        managedservice = "org.tawja.platform.plugins.database.hsql.server"
)
@Provides
@Whiteboards(
        whiteboards = {
            @Wbp(
                    filter = "(objectclass=org.tawja.platform.plugins.database.hsql.impl.HsqldbDatabase)",
                    onArrival = "onArrivalDatabase",
                    onDeparture = "onDepartureDatabase",
                    onModification = "onModificationDatabase")
        }
)
public class HsqldbDatabaseServer extends AbstractTwjDatabaseServer<Server> {

    private static final Logger LOG = LoggerFactory.getLogger(HsqldbDatabaseFactory.class);

    @Requires(filter = "(factory.name=org.tawja.platform.plugins.database.hsql.database)")
    private Factory databasefactory;

    public HsqldbDatabaseServer() {
        super();
        setType(HsqldbServiceProperties.SERVER_TYPE_NAME);
    }

    public void createInnerServer() {
        Server hsqldbServer = new Server();
        //hsqldbServer.setDatabaseName(0, "default");
        //hsqldbServer.setDatabasePath(0, "databases/hsqldb/" + getName() + "/default");
        // hsqldbServer.setRestartOnShutdown (true);
        //hsqldbServer.setAddress(getHost());
        hsqldbServer.setPort(getPort());
        hsqldbServer.setSilent(false);

        setInnerServer(hsqldbServer);

        //return getInnerServer();
    }

    public void createDefaultDatabase() {
        Properties config = new Properties();
        config.put("instance.name", TwjDatabaseUtils.getDatabaseKey("db-" + HsqldbServiceProperties.SERVER_TYPE_NAME, "default", "default"));
        config.put("name", "default");
        config.put("serverName", "default");
        config.put("path", HsqldbServiceProperties.CONF_DB_PATH_PREFIX + "/" + getName() + "/default");
        config.put("url", HsqldbServiceProperties.CONF_DB_URL_PREFIX + "/" + getHost() + ":" + getPort() + "/" + getName() + "/default");
        config.put("user", "");
        config.put("password", "");
        ComponentInstance instance = null;
        try {
            instance = databasefactory.createComponentInstance(config);
        } catch (UnacceptableConfiguration ucEx) {
            LOG.error("Unable to create HSQL Database instance : Bad configuration", ucEx);
        } catch (MissingHandlerException mhEx) {
            LOG.error("Unable to create HSQL Database instance : Missing handler", mhEx);
        } catch (ConfigurationException cEx) {
            LOG.error("Unable to create HSQL Database instance : erron in configuration", cEx);
        }

        //TwjDatabaseServer server = ipojoHelper.getServiceObjectByName(TwjDatabaseServer.class, instance.getInstanceName());
    }

    public void destroyInnerServer() {
        setInnerServer(null);
    }

    @Override
    @Validate
    public void start() {
        super.start();
        createInnerServer();
        getInnerServer().start();
        LOG.info("HSQLDB Database Server State  : " + getInnerServer().getStateDescriptor());
        LOG.info("HSQLDB Database Server Adress : " + getInnerServer().getAddress());
        LOG.info("HSQLDB Database Server Port   : " + getInnerServer().getPort());
        createDefaultDatabase();
        LOG.info("HSQLDB Default Database created");
    }

    @Override
    @Invalidate
    public void stop() {
        super.stop();
        getInnerServer().stop();
        destroyInnerServer();
    }

    @Override
    @Updated
    public void update() {
        super.update();
    }

    @Override
    public TwjServerStatus getStatus() {
        return TwjServerStatus.UNKNOWN;
    }

}
