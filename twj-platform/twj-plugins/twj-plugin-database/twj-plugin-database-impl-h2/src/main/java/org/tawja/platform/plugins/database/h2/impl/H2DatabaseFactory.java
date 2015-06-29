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

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.h2.tools.Server;
import org.tawja.platform.plugins.database.base.AbstractTwjDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.plugins.database.h2.H2ServiceProperties;

/**
 *
 * @author jbennani
 */
@Component(publicFactory=false)
@Instantiate
public class H2DatabaseFactory extends AbstractTwjDatabaseFactory<Server> {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(H2DatabaseFactory.class);

    public H2DatabaseFactory() {
        super();
        setName(H2ServiceProperties.SERVER_TYPE_NAME);
        LOG.debug(H2DatabaseFactory.class + " instanciated.");
    }

}
