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
package org.tawja.platform.plugins.database.base.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.database.api.TwjDatabase;
import org.tawja.platform.plugins.database.api.TwjDatabaseContainer;
import org.tawja.platform.plugins.database.api.TwjDatabaseServer;
import org.tawja.platform.plugins.database.base.TwjDatabaseUtils;

/**
 *
 * @author jbennani
 */
@Component(publicFactory = false)
@Provides
@Instantiate
@Whiteboards(
        whiteboards = {
            @Wbp(
                    filter = "(objectclass=org.tawja.platform.plugins.database.api.TwjDatabaseServer)",
                    onArrival = "onArrivalDatabaseServer",
                    onDeparture = "onDepartureDatabaseServer",
                    onModification = "onModificationDatabaseServer"),
            @Wbp(
                    filter = "(objectclass=org.tawja.platform.plugins.database.api.TwjDatabase)",
                    onArrival = "onArrivalDatabase",
                    onDeparture = "onDepartureDatabase",
                    onModification = "onModificationDatabase")
        }
)
public class TwjDatabaseContainerImpl implements TwjDatabaseContainer {

    private static final Logger LOG = LoggerFactory.getLogger(TwjDatabaseContainerImpl.class);

    @Context
    private BundleContext context;

    private Map<String, TwjDatabaseServer> servers;
    private Map<String, TwjDatabase> databases; // Enable to have in memory databases or simple connections not linked to a server

    public TwjDatabaseContainerImpl() {
        servers = new HashMap<String, TwjDatabaseServer>();
    }

    public synchronized void onArrivalDatabaseServer(ServiceReference ref) {
        LOG.debug("Server arrival.");
        TwjDatabaseServer server = (TwjDatabaseServer) context.getService(ref);
        servers.put(TwjDatabaseUtils.getDatabaseServerKey(server), server);
    }

    public synchronized void onDepartureDatabaseServer(ServiceReference ref) {
        LOG.debug("Server departure.");
        TwjDatabaseServer server = (TwjDatabaseServer) context.getService(ref);
        servers.remove(server);
    }

    public synchronized void onModificationDatabaseServer(ServiceReference ref) {
        LOG.debug("Server modification.");
        //TwjDatabaseServer server = (TwjDatabaseServer) context.getService(ref);
        //server.restart();
    }

    public synchronized void onArrivalDatabase(ServiceReference ref) {
        LOG.debug("Database arrival.");
        TwjDatabase database = (TwjDatabase) context.getService(ref);
        databases.put(TwjDatabaseUtils.getDatabaseKey(database), database);
    }

    public synchronized void onDepartureDatabase(ServiceReference ref) {
        LOG.debug("Database departure.");
        TwjDatabase database = (TwjDatabase) context.getService(ref);
        databases.remove(database);
    }

    public synchronized void onModificationDatabase(ServiceReference ref) {
        LOG.debug("Database modification.");
        //TwjDatabase database = (TwjDatabase) context.getService(ref);
        //database.restart();
    }

    @Override
    public Map<String, TwjDatabaseServer> getServers() {
        return servers;
    }

    @Override
    public Map<String, TwjDatabase> getDatabases() {
        return databases;
    }
}
