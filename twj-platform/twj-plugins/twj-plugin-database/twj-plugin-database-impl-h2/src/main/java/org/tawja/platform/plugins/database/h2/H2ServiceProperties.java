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
package org.tawja.platform.plugins.database.h2;

/**
 *
 * @author jbennani
 */
public class H2ServiceProperties {
    public static final String SERVER_TYPE_NAME = "h2";

    public static final String CONF_DB_DRIVER_NAME = "org.h2.Driver";
    //public static final String CONF_DB_URL_PREFIX = "jdbc:h2:";
    public static final String CONF_DB_URL_PREFIX = "jdbc:h2:tcp://";
    public static final String CONF_DB_PATH_PREFIX = "workspace/databases/" + SERVER_TYPE_NAME;

    public static final String PROP_LOG = "server-h2.log";
}
