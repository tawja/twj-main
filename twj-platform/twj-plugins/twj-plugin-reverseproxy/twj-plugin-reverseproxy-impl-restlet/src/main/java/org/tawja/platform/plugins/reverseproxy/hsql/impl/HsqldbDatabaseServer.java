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
package org.tawja.platform.plugins.reverseproxy.hsql.impl;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.hsqldb.server.Server;
import org.tawja.platform.plugins.reverseproxy.base.AbstractTwjDatabaseServer;
import org.tawja.platform.plugins.reverseproxy.api.TwjServerStatus;
import org.tawja.platform.plugins.reverseproxy.restlet.ReverseProxyServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jbennani
 */
@Component(
        name="org.tawja.platform.plugins.database.hsql.server",
        managedservice="org.tawja.platform.plugins.database.hsql.server"
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

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(HsqldbDatabaseFactory.class);

    public HsqldbDatabaseServer() {
        super();
        setName("default");
        setType(ReverseProxyServiceProperties.SERVER_TYPE_NAME);
    }
    
    public Server createInnerServer() {
        Server hsqldbServer = new Server();
        hsqldbServer.setDatabaseName(0, "default");
        hsqldbServer.setDatabasePath(0, "databases/hsqldb/default");
        // hsqldbServer.setRestartOnShutdown (true);
        //hsqldbServer.setAddress(host);
        hsqldbServer.setPort(getPort());
        hsqldbServer.setSilent(false);

        setInnerServer(hsqldbServer);

        return getInnerServer();
    }

    public void destroyInnerServer() {
        setInnerServer(null);
    }

    @Override
    public Boolean start() {
        super.start();
        createInnerServer();
        getInnerServer().start();
        LOG.info("HSQLDB Database Server State  : " + getInnerServer().getStateDescriptor());
        LOG.info("HSQLDB Database Server Adress : " + getInnerServer().getAddress());
        LOG.info("HSQLDB Database Server Port   : " + getInnerServer().getPort());
        return true;
    }

    @Override
    public Boolean stop() {
        super.stop();
        getInnerServer().stop();
        destroyInnerServer();
        return true;
    }

    @Override
    public TwjServerStatus getStatus() {
        return TwjServerStatus.UNKNOWN;
    }

}
