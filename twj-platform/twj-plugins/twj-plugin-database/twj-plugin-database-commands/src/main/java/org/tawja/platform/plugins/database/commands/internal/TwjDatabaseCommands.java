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
package org.tawja.platform.plugins.database.commands.internal;

import java.util.Map;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.service.command.Descriptor;
import org.tawja.platform.plugins.database.api.TwjDatabase;
import org.tawja.platform.plugins.database.api.TwjDatabaseFactory;
import org.tawja.platform.plugins.database.api.TwjDatabaseServer;
import org.tawja.platform.plugins.database.api.TwjDatabaseService;

/**
 * ConfigurationAdmin commands giving utilities for configuration
 *
 * @author jbennani
 */
@Component(publicFactory = false, immediate = true)
@Instantiate
@Provides
public class TwjDatabaseCommands {

    /**
     * Defines the command scope (ipojo).
     */
    @ServiceProperty(name = "osgi.command.scope", value = "tawja")
    String m_scope;

    /**
     * Defines the functions (commands).
     */
    @ServiceProperty(name = "osgi.command.function", value = "{}")
    String[] m_function = new String[]{
        "dbListFactories",
        "dbListServers",
        "dbListDatabases"
    };

    /**
     * Configuration Admin service dependency.
     */
    @Requires
    TwjDatabaseService tds;

    /**
     * Command list factories registered in the TwjDatabaseService
     */
    @Descriptor("Command list factories registered in the TwjDatabaseService")
    public void dbListFactories() {
        Map<String, TwjDatabaseFactory> factoryMap = null;
        factoryMap = tds.getControler().getFactories();
        for (TwjDatabaseFactory factory : factoryMap.values()) {
            System.out.println(factory.getType() + " : " + factory.getName());
        }
    }

    /**
     * Command list servers registered in the TwjDatabaseService
     */
    @Descriptor("Command list servers registered in the TwjDatabaseService")
    public void dbListServers() {
        Map<String, TwjDatabaseServer> serverMap = null;
        serverMap = tds.getControler().getContainer().getServers();
        for (TwjDatabaseServer server : serverMap.values()) {
            System.out.println(server.getType() + " : " + server.getName() + " : " + server.getHost() + " : " + server.getPort() + " : " + server.getStatus());
        }
    }
    
    /**
     * Command list databases registered in the TwjDatabaseService
     */
    @Descriptor("Command list databases registered in the TwjDatabaseService")
    public void dbListDatabases() {
        Map<String, TwjDatabase> databaseMap = null;
        databaseMap = tds.getControler().getContainer().getDatabases();
        for (TwjDatabase database : databaseMap.values()) {
            System.out.println(database.getServerType() + " : " + database.getServerName() + " : " + database.getName() + " : " + database.getUrl() + " : " + database.getPath());
        }
    }

}
