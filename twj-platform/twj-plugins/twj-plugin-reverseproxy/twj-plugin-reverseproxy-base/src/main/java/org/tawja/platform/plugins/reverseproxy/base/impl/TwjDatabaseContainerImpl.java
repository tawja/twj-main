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
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseContainer;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseFactory;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseServer;
import org.tawja.platform.plugins.reverseproxy.base.AbstractTwjDatabaseServer;

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
                    onModification = "onModificationDatabaseServer")
        }
)
public class TwjDatabaseContainerImpl implements TwjDatabaseContainer {
    private static final Logger LOG = LoggerFactory.getLogger(TwjDatabaseContainerImpl.class);

    @Context
    private BundleContext context;

    private Map<String, TwjDatabaseServer> servers;

    public TwjDatabaseContainerImpl() {
        servers = new HashMap<String, TwjDatabaseServer>();
    }

    public synchronized void onArrivalDatabaseServer(ServiceReference ref) {
        LOG.debug("Server arrival.");
        TwjDatabaseServer server = (TwjDatabaseServer) context.getService(ref);
        servers.put(server.getName(), server);
        
        // to remove
        //server.createInnerServer();
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
    

    @Override
    public Map<String, TwjDatabaseServer> getServers() {
        return servers;
    }

}
