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
package org.tawja.platform.plugins.reverseproxy.base;

import org.tawja.platform.plugins.reverseproxy.api.TwjServerStatus;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabase;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseServer;
import java.util.ArrayList;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Validate;
import org.apache.felix.ipojo.whiteboard.Wbp;
import org.apache.felix.ipojo.whiteboard.Whiteboards;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jbennani
 */
@Whiteboards(
        whiteboards = {
            @Wbp(
                    filter = "(objectclass=org.tawja.platform.plugins.database.api.TwjDatabase)",
                    onArrival = "onArrivalDatabase",
                    onDeparture = "onDepartureDatabase",
                    onModification = "onModificationDatabase")
        }
)
public abstract class AbstractTwjDatabaseServer<T> implements TwjDatabaseServer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTwjDatabaseServer.class);

    @Context
    protected BundleContext context;

    @Property(value = "default")
    private String name;
    @Property(value = "default")
    private String type;

    @Property(value = "true")
    private Boolean autoStart;

    @Property(value = "localhost")
    private String host;
    @Property(value = "9010")
    private int port;

    private T innerServer;
    private ArrayList<TwjDatabase> databases;


    /**
     * Avoid default constructor
     */
    public AbstractTwjDatabaseServer() {
        databases = new ArrayList<TwjDatabase>();
    }

    @Override
    @Validate
    public Boolean start() {
        return false;
    }

    @Override
    @Invalidate
    public Boolean stop() {
        return false;
    }

    @Override
    public TwjServerStatus getStatus() {
        return TwjServerStatus.UNKNOWN;
    }

    public synchronized void onArrivalDatabase(ServiceReference ref) {
        LOG.debug("Database arrival.");
        TwjDatabase database = (TwjDatabase) context.getService(ref);
        getDatabases().add(database);
    }

    public synchronized void onDepartureDatabase(ServiceReference ref) {
        LOG.debug("Database departure.");
        TwjDatabase database = (TwjDatabase) context.getService(ref);
        getDatabases().remove(database);
    }

    public synchronized void onModificationDatabase(ServiceReference ref) {
        LOG.debug("Database modification.");
        TwjDatabase database = (TwjDatabase) context.getService(ref);
        //database.restart();
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @return the databases
     */
    @Override
    public ArrayList<TwjDatabase> getDatabases() {
        return databases;
    }

    /**
     * @return the autoStart
     */
    @Override
    public Boolean isAutoStart() {
        return getAutoStart();
    }

    /**
     * @return the host
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * @return the port
     */
    @Override
    public int getPort() {
        return port;
    }

    @Override
    public T getInnerServer() {
        return innerServer;
    }

    /**
     * @param name the name to set
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * @param type the type to set
     */
    protected void setType(String type) {
        this.type = type;
    }

    /**
     * @param databases the databases to set
     */
    protected void setDatabases(ArrayList<TwjDatabase> databases) {
        this.databases = databases;
    }

    /**
     * @return the autoStart
     */
    protected Boolean getAutoStart() {
        return autoStart;
    }

    /**
     * @param autoStart the autoStart to set
     */
    protected void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    /**
     * @param host the host to set
     */
    protected void setHost(String host) {
        this.host = host;
    }

    /**
     * @param port the port to set
     */
    protected void setPort(int port) {
        this.port = port;
    }

    /**
     * @param innerServer the innerServer to set
     */
    protected void setInnerServer(T innerServer) {
        this.innerServer = innerServer;
    }

}
