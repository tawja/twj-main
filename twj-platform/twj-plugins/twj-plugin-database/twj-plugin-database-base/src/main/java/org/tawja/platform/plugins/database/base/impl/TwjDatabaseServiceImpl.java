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

import java.util.Map;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.tawja.platform.plugins.database.api.TwjDatabaseControler;
import org.tawja.platform.plugins.database.api.TwjDatabaseFactory;
import org.tawja.platform.plugins.database.api.TwjDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.database.api.TwjDatabase;
import org.tawja.platform.plugins.database.api.TwjDatabaseServer;

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
                    filter = "(objectclass=org.tawja.platform.plugins.database.api.TwjDatabaseFactory)",
                    onArrival = "onArrivalDatabaseFactory",
                    onDeparture = "onDepartureDatabaseFactory",
                    onModification = "onModificationDatabaseFactory"),
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
public class TwjDatabaseServiceImpl implements TwjDatabaseService {

    private static final Logger LOG = LoggerFactory.getLogger(TwjDatabaseServiceImpl.class);

    TwjDatabaseControler dbControler = new TwjDatabaseControlerImpl();

    @Context
    private BundleContext context;

    public TwjDatabaseServiceImpl() {
        LOG.debug(TwjDatabaseServiceImpl.class + " created.");
        dbControler = new TwjDatabaseControlerImpl();
    }

    public synchronized void onArrivalDatabaseFactory(ServiceReference ref) {
        LOG.debug("Database Factory arrival.");
        //dbControler.addFactory((TwjDatabaseFactory) context.getService(ref));
    }

    public synchronized void onDepartureDatabaseFactory(ServiceReference ref) {
        LOG.debug("Database Factory departure.");
        //dbControler.removeFactory((TwjDatabaseFactory) context.getService(ref));
    }

    public synchronized void onModificationDatabaseFactory(ServiceReference ref) {
        LOG.debug("Database Factory modification.");
        // do something
    }

    public synchronized void onArrivalDatabaseServer(ServiceReference ref) {
        LOG.debug("Server arrival.");
        //TwjDatabaseServer server = (TwjDatabaseServer) context.getService(ref);
        //dbControler.getContainer().addServer((server.getName());
    }

    public synchronized void onDepartureDatabaseServer(ServiceReference ref) {
        LOG.debug("Server departure.");
        //TwjDatabaseServer server = (TwjDatabaseServer) context.getService(ref);
        //dbControler.getContainer().removeServer(server.getName());
    }

    public synchronized void onModificationDatabaseServer(ServiceReference ref) {
        LOG.debug("Server modification.");
        //TwjDatabaseServer server = (TwjDatabaseServer) context.getService(ref);
        //server.restart();
    }

    public synchronized void onArrivalDatabase(ServiceReference ref) {
        LOG.debug("Database arrival.");
        //TwjDatabase database = (TwjDatabase) context.getService(ref);
        //dbControler.getContainer().getServer(database.getServerName()).addDatabase(database);
    }

    public synchronized void onDepartureDatabase(ServiceReference ref) {
        LOG.debug("Database departure.");
        //TwjDatabase database = (TwjDatabase) context.getService(ref);
        //dbControler.getContainer().getServer(database.getServerName()).removeDatabase((TwjDatabase) context.getService(ref));
    }

    public synchronized void onModificationDatabase(ServiceReference ref) {
        LOG.debug("Database modification.");
        //TwjDatabase database = (TwjDatabase) context.getService(ref);
        //database.restart();
    }

    @Override
    public Map<String, TwjDatabaseFactory> getFactories() {
        return dbControler.getFactories();
    }

    @Override
    public TwjDatabaseControler getControler() {
        return dbControler;
    }
    /*
     @Override
     public TwjDatabaseServer createtServer(String serverType) {
     TwjDatabaseServer server = dbControler.createtServer(serverType);
     return server;
     }
     */
}
