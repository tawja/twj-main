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

import java.sql.SQLException;
import javax.sql.ConnectionPoolDataSource;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Updated;
import org.apache.felix.ipojo.annotations.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.database.api.TwjDatabase;
import org.tawja.platform.plugins.database.jdbc.MiniConnectionPoolManager;

/**
 *
 * @author jbennani
 */
public abstract class AbstractTwjDatabase<T> implements TwjDatabase<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractTwjDatabaseServer.class);

    private MiniConnectionPoolManager poolMgr;

    @Property(value = "default")
    private String name;

    // Not a property as forced by the child class
    //@Property(value = "default")
    private String serverType;

    @Property(value = "default")
    private String serverName;
    @Property(value = "")
    private String path;
    @Property(value = "")
    private String url;

    @Property(value = "")
    private String user;
    @Property(value = "")
    private String password;

    @Property(value = "10")
    private int poolMaxConnections;     // Number of max connections in the pool
    @Property(value = "60")
    private int poolTimeout;     // Number of seconds before timeout
    @Property(value = "60")
    private int poolMaxIdleConnectionLife;     // Number of max connections inactive life time in second before dispose

    /**
     * Avoid default constructor
     */
    public AbstractTwjDatabase() {
        super();
    }

    @Override
    @Validate
    public void start() {
        createDatabasePool();
    }

    @Override
    @Invalidate
    public void stop() {
        try {
            poolMgr.dispose();
        } catch (SQLException sqlEx) {
            LOG.error("Error while disposing connection pool for database '" + TwjDatabaseUtils.getDatabaseKey(this) + "'", sqlEx);
        }
    }

    @Override
    @Updated
    public void update() {
        stop();
        start();
    }

    public abstract ConnectionPoolDataSource createDataSource();

    public void createDatabasePool() {
        ConnectionPoolDataSource dataSource = createDataSource();
        poolMgr = new MiniConnectionPoolManager(dataSource, getPoolMaxConnections(), getPoolTimeout(), getPoolMaxIdleConnectionLife());
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the serverName
     */
    @Override
    public String getServerName() {
        return serverName;
    }

    /**
     * @return the path
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * @return the url
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * @return the user
     */
    @Override
    public String getUser() {
        return user;
    }

    /**
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @return the serverType
     */
    @Override
    public String getServerType() {
        return serverType;
    }

    /**
     * @param name the name to set
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * @param serverType the serverType to set
     */
    protected void setServerType(String serverType) {
        this.serverType = serverType;
    }

    /**
     * @param serverName the serverName to set
     */
    protected void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @param path the path to set
     */
    protected void setPath(String path) {
        this.path = path;
    }

    /**
     * @param url the url to set
     */
    protected void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param user the user to set
     */
    protected void setUser(String user) {
        this.user = user;
    }

    /**
     * @param password the password to set
     */
    protected void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the poolMaxConnections
     */
    public int getPoolMaxConnections() {
        return poolMaxConnections;
    }

    /**
     * @param poolMaxConnections the poolMaxConnections to set
     */
    protected void setPoolMaxConnections(int poolMaxConnections) {
        this.poolMaxConnections = poolMaxConnections;
    }

    /**
     * @return the poolTimeout
     */
    public int getPoolTimeout() {
        return poolTimeout;
    }

    /**
     * @param poolTimeout the poolTimeout to set
     */
    protected void setPoolTimeout(int poolTimeout) {
        this.poolTimeout = poolTimeout;
    }

    /**
     * @return the poolMaxIdleConnectionLife
     */
    public int getPoolMaxIdleConnectionLife() {
        return poolMaxIdleConnectionLife;
    }

    /**
     * @param poolMaxIdleConnectionLife the poolMaxIdleConnectionLife to set
     */
    protected void setPoolMaxIdleConnectionLife(int poolMaxIdleConnectionLife) {
        this.poolMaxIdleConnectionLife = poolMaxIdleConnectionLife;
    }
}
