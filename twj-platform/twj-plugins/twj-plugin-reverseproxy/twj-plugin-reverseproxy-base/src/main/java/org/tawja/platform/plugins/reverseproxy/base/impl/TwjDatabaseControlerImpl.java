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
package org.tawja.platform.plugins.reverseproxy.base.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseContainer;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseControler;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseFactory;
import org.tawja.platform.plugins.reverseproxy.base.AbstractTwjDatabaseServer;

/**
 *
 * @author jbennani
 */
@Component(publicFactory = false)
@Provides
@Instantiate
public class TwjDatabaseControlerImpl implements TwjDatabaseControler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTwjDatabaseServer.class);

    @Context
    private BundleContext context;

    @Requires
    TwjDatabaseContainer dbContainer;

    private Map<String, TwjDatabaseFactory> dbFactories;

    public TwjDatabaseControlerImpl() {
        //dbContainer = new TwjDatabaseContainerImpl();
        dbFactories = new HashMap<String, TwjDatabaseFactory>();
    }

    public synchronized void onArrivalDatabaseFactory(ServiceReference ref) {
        LOG.debug("Database Factory arrival.");
        TwjDatabaseFactory factory = (TwjDatabaseFactory) context.getService(ref);
        dbFactories.put(factory.getName(), factory);
    }

    public synchronized void onDepartureDatabaseFactory(ServiceReference ref) {
        LOG.debug("Database Factory departure.");
        TwjDatabaseFactory factory = (TwjDatabaseFactory) context.getService(ref);
        dbFactories.remove(factory);
    }

    public synchronized void onModificationDatabaseFactory(ServiceReference ref) {
        LOG.debug("Database Factory modification.");
        // do something
    }

    /**
     * @return the dbContainer
     */
    @Override
    public TwjDatabaseContainer getContainer() {
        return dbContainer;
    }

    private String getServerName(String name, String type, String host, int port) {
        return (name + "-" + type + "-" + host + "-" + port);
    }

    /**
     * @return the dbFactories
     */
    @Override
    public Map<String, TwjDatabaseFactory> getFactories() {
        return dbFactories;
    }
    /*
     @Override
     public TwjDatabaseServer createtServer(String serverType) {
     TwjDatabaseServer server = null;
     TwjDatabaseFactory factory = dbFactories.get(serverType);
     if(factory != null)
     {
     dbContainer.checkBeforeServerCreation("testdb", serverType, "localhost", 9010);
            
     //String serverName = getServerName("testdb", serverType, "localhost", 9010);
     server = factory.createDatabaseServer("testdb", "localhost", 9010);
     dbContainer.addDatabase(server);
     }
     return server;
     }
     */
}
