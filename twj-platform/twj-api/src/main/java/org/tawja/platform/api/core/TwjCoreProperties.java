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
public class TwjCoreProperties {

    /**
     * The property name prefix for the launcher's auto-install property.
     */
    public static final String AUTO_INSTALL_PROP = "tawja.auto.install";
    
    /**
     * The property name prefix for the launcher's auto-start property.
     */
    public static final String AUTO_START_PROP = "tawja.auto.start";
    
    /**
     * The property name used to specify Bundles Configuration Directory.
     */
    public static final String BUNDLES_CONFIG_DIR_PROP = "tawja.bundles.config.dir";
    
    /**
     * The property name used to specify Bundles Directory.
     */
    public static final String BUNDLES_DIR_PROP = "tawja.bundles.dir";
    
    /**
     * Name of the environment property to clear bundle cache.
     */
    public static final String CLEAR_CACHE_PROP = "tawja.clear.cache";
    
    /**
     * The property name used to specify an URL to the configuration property
     * file to be used for the created the framework instance.
     */
    public static final String CONFIG_PROPERTIES_PROP = "tawja.config.properties";
    
    /**
     * The property name used to specify whether the launcher should install a
     * shutdown hook.
     */
    public static final String SHUTDOWN_HOOK_PROP = "tawja.shutdown.hook";
    
    /**
     * The property name used to specify an URL to the system property file.
     */
    public static final String SYSTEM_PROPERTIES_PROP = "tawja.system.properties";
    
    /**
     * The property name used to specify the HOME directory.
     */
    public static final String HOME_DIR_PROP = "tawja.home.dir";
    
    /**
     * The property name used to specify the BASE directory.
     */
    public static final String BASE_DIR_PROP = "tawja.base.dir";
}
