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
package org.tawja.platform.plugins.database.h2.impl;

import java.sql.SQLException;
import java.util.ArrayList;
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
import org.h2.tools.Server;
import org.tawja.platform.plugins.database.base.AbstractTwjDatabaseServer;
import org.tawja.platform.plugins.database.api.TwjServerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.database.base.TwjDatabaseUtils;
import org.tawja.platform.plugins.database.h2.H2ServiceProperties;

/**
 *
 * @author jbennani
 */
@Component(
        name = "org.tawja.platform.plugins.database.h2.server",
        managedservice = "org.tawja.platform.plugins.database.h2.server"
)
@Provides
@Whiteboards(
        whiteboards = {
            @Wbp(
                    filter = "(objectclass=org.tawja.platform.plugins.database.h2.impl.H2Database)",
                    onArrival = "onArrivalDatabase",
                    onDeparture = "onDepartureDatabase",
                    onModification = "onModificationDatabase")
        }
)
public class H2DatabaseServer extends AbstractTwjDatabaseServer<Server> {

    private static final Logger LOG = LoggerFactory.getLogger(H2DatabaseFactory.class);

    @Requires(filter = "(factory.name=org.tawja.platform.plugins.database.h2.database)")
    private Factory databasefactory;

    public H2DatabaseServer() {
        super();
        setName("default");
        setType(H2ServiceProperties.SERVER_TYPE_NAME);
    }

    public Server createInnerServer() {
        ArrayList<String> args = new ArrayList<String>();
        args.add("-baseDir");
        args.add("./databases/h2/" + getName());
        //args.add("-trace");
        args.add("-tcpAllowOthers");
        //args.add("-tcpDaemon");
        //args.add("-tcpSSL");
        //args.add("-key");
        args.add("-tcpPort");
        args.add("9110");
        args.add("-tcpPassword");
        args.add("");
        args.add("-ifExists");

        try {
            Server h2Server = Server.createTcpServer(args.toArray(new String[args.size()]));
            setInnerServer(h2Server);
        } catch (SQLException sqlEx) {
            LOG.error("Unable to create H2 Database Server.", sqlEx);
        }

        return getInnerServer();
    }

    public void createDefaultDatabase() {
        Properties config = new Properties();
        config.put("instance.name", TwjDatabaseUtils.getDatabaseKey("db-" + H2ServiceProperties.SERVER_TYPE_NAME, "default", "default"));
        config.put("name", "default");
        config.put("serverName", "default");
        config.put("path", H2ServiceProperties.CONF_DB_PATH_PREFIX + "/" + getName() + "/default");
        config.put("url", H2ServiceProperties.CONF_DB_URL_PREFIX + "/" + getHost() + ":" + getPort() + "/" + getName() + "/default");
        config.put("user", "");
        config.put("password", "");
        ComponentInstance instance = null;
        try {
            instance = databasefactory.createComponentInstance(config);
        } catch (UnacceptableConfiguration ucEx) {
            LOG.error("Unable to create H2 Database instance : Bad configuration", ucEx);
        } catch (MissingHandlerException mhEx) {
            LOG.error("Unable to create H2 Database instance : Missing handler", mhEx);
        } catch (ConfigurationException cEx) {
            LOG.error("Unable to create H2 Database instance : erron in configuration", cEx);
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

        try {
            getInnerServer().start();
        } catch (SQLException sqlEx) {
            LOG.error("Unable to start H2 Database Server.", sqlEx);
        }

        LOG.info("H2 Database Server State  : " + getInnerServer().getStatus());
        LOG.info("H2 Database Server Adress : " + getInnerServer().getURL());
        LOG.info("H2 Database Server Port   : " + getInnerServer().getPort());

        createDefaultDatabase();
        LOG.info("H2 Default Database created");
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
