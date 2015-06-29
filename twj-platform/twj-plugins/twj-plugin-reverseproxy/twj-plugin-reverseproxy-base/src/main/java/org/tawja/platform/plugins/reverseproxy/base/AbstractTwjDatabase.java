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

import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Property;
import org.apache.felix.ipojo.annotations.Validate;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabase;
import org.tawja.platform.plugins.reverseproxy.api.TwjDatabaseServer;

/**
 *
 * @author jbennani
 */
public abstract class AbstractTwjDatabase<T> implements TwjDatabase<T> {

    protected TwjDatabaseServer<T> innerServer;

    @Property(value = "default")
    private String name;
    @Property(value = "default")
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

    /**
     * Avoid default constructor
     */
    public AbstractTwjDatabase() {
        super();

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
     * @return the innerServer
     */
    @Override
    public TwjDatabaseServer<T> getInnerServer() {
        return innerServer;
    }

    /**
     * @param innerServer the innerServer to set
     */
    protected void setInnerServer(TwjDatabaseServer<T> innerServer) {
        this.innerServer = innerServer;
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
}
