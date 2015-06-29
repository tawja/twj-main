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
package org.tawja.platform.plugins.database.h2.impl;

import javax.sql.ConnectionPoolDataSource;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Updated;
import org.h2.tools.Server;
import org.tawja.platform.plugins.database.base.AbstractTwjDatabase;
import org.tawja.platform.plugins.database.h2.H2ServiceProperties;

/**
 *
 * @author jbennani
 */
@Component(
        name = "org.tawja.platform.plugins.database.h2.database",
        managedservice = "org.tawja.platform.plugins.database.h2.database"
)
public class H2Database extends AbstractTwjDatabase<Server> {

    public H2Database() {
        super();
        setServerType(H2ServiceProperties.SERVER_TYPE_NAME);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    @Updated
    public void update() {
        super.update();
    }

    @Override
    public ConnectionPoolDataSource createDataSource() {
        org.h2.jdbcx.JdbcDataSource dataSource = new org.h2.jdbcx.JdbcDataSource();
        dataSource.setURL(getUrl());
        return dataSource;
    }

}
