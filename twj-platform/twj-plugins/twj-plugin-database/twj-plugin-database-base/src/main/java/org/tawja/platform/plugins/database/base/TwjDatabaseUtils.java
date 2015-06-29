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
package org.tawja.platform.plugins.database.base;

import org.tawja.platform.plugins.database.base.impl.*;
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
public class TwjDatabaseUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TwjDatabaseUtils.class);

    public static String getDatabaseServerKey(TwjDatabaseServer server) {
        return getDatabaseServerKey(server.getType(), server.getName());
    }

    public static String getDatabaseServerKey(String srvType, String srvName) {
        String serverKey
                = (srvType != null ? srvType : "void")
                + "-" + srvName;
        return serverKey;
    }

    public static String getDatabaseKey(TwjDatabase database) {
        return getDatabaseKey(database.getServerType(), database.getServerName(), database.getName());
    }

    public static String getDatabaseKey(String dbSrvType, String dbSrvName, String dbName) {
        String databaseKey
                = (dbSrvType != null ? dbSrvType : "void")
                + "-" + (dbSrvName != null ? dbSrvName : "void")
                + "-" + dbName;
        return databaseKey;
    }
}
