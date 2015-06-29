/**
 * Copyright (C) 2015 Tawja (http://www.tawja.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tawja.platform.api.core;

/**
 * Contains definitions of constants for Core module, which can be used across
 * all platforms and modules.
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class TwjCoreConstants {


    /**
     * Amount of time to wait for framework to shut down.
     */
    public static final long CONF_STOP_TIMEOUT = 2000;

    /**
     * Jar file extension.
     */
    public static final String JAR_EXTENSION = ".jar";
    
    /**
     * Environement variable to define the HOME directory.
     * (where binaries are...)
     */
    public static final String APPLICATION_HOME_VAR = "TAWJA_HOME";

    /**
     * Environement variable to define the BASE directory.
     * (where working files are...)
     */
    public static final String APPLICATION_BASE_VAR = "TAWJA_BASE";
    
    /**
     * Switch for specifying bundle directory.
     */
    public static final String SWITCH_CONFIG_DIR = "-config";
    
    /**
     * Switch for specifying base configuration directory.
     */
    public static final String SWITCH_BUNDLES_DIR = "-deploy";
    
    /**
     * Switch for specifying force clear caches.
     */
    public static final String SWITCH_CLEAR_CACHES = "-clear";
    
    /**
     * Switch for specifying force to work in current directory
     * (no HOME var use, nor any logic... just current directory).
     */
    public static final String SWITCH_FORCE_CURRDIR = "-currdir";
    
    /**
     * Switch for specifying application name.
     */
    public static final String SWITCH_APPLICATION_NAME = "-application";

    /**
     * Switch for specifying context name.
     */
    public static final String SWITCH_CONTEXT_NAME = "-context";

    /**
     * Switch for specifying instance name.
     */
    public static final String SWITCH_INSTANCE_NAME = "-instance";

    /**
     * Prefix for application directory.
     */
    public static final String STORE_PREFIX = ".";

    /**
     * Name of application. Used as a storage directory. This is hidden
     * directory in user home directory (dot added as a prefix).
     */
    public static final String APPLICATION_NAME = "tawja";

    /**
     * Name of application context (default, prod, dev, test).
     */
    public static final String APPLICATION_CONTEXT_NAME = "default";

    /**
     * Name of application instance (master, healthcheck, reverseproxy, server,
     * console, desktop, ...).
     */
    public static final String APPLICATION_INSTANCE_NAME = "master";

    /**
     * Name of main jar application container used to check current directory
     */
    public static final String MAIN_JAR_CONTAINER_NAME = "twj-container.jar";

    /**
     * Default value force clear cache
     */
    public static final Boolean DEFAULT_CLEAR_CACHE = false;

    /**
     * Default value force use current directory
     */
    public static final Boolean DEFAULT_FORCE_CURRDIR = false;

    /**
     * Name of directory containing bundles cache.
     */
    public static final String BUNDLES_CACHE_DIR_NAME = "cache";

    /**
     * Default directory where bundles are stored.
     */
    public static final String BUNDLES_DIR_NAME = "bundles";

    /**
     * Default directory where configuration are stored.
     */
    public static final String BUNDLES_CONFIG_DIR_NAME = "config";

    /**
     * Default directory where temporary files are stored.
     */
    public static final String TEMP_DIR_NAME = "tmp";

    /**
     * Default directory where working files are stored.
     */
    public static final String WORK_DIR_NAME = "workspace";

    /**
     * File containing names of packages that should be exported from system
     * bundle.
     */
    public static final String EXTRA_PACKAGES_FILE_NAME = "/extra-packages";

    /**
     * Name of external base configuration file.
     */
    public static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    /**
     * The default name used for the system properties file.
     *
     */
    public static final String SYSTEM_PROPERTIES_FILE_NAME = "system.properties";
}
