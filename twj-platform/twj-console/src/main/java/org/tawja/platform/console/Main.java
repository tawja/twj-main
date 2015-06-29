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
package org.tawja.platform.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.tawja.platform.api.TwjApplicationStarter;
import org.tawja.platform.core.TwjApplication;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.launch.Framework;

/**
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class Main implements TwjApplicationStarter {

    // Initialize Java Logging Framework with default formatter
    static {
        final InputStream inputStream = Main.class.getResourceAsStream("/logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }

    /**
     * Starter instance
     */
    private static Main main;
    /**
     * Application instance
     */
    private static TwjApplication application;
    /**
     * Entry point arguments
     */
    private String[] args;
    /**
     * Configuration map.
     */
    private static Map<String, Object> config;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        main = new Main();
        main.start(args);
    }

    private void start(String[] args) {
        application = new TwjApplication(this);
        application.init();
        application.start();
    }

    /**
     *
     * @return
     */
    @Override
    public String[] getArgs() {
        return args;
    }

    /**
     *
     * @return
     */
    @Override
    public Framework getOsgiFrameworkContainer() {
        Felix framework = new Felix(config);
        return framework;
    }

    /**
     *
     * @param config
     * @param activators
     */
    @Override
    public void configureOsgiFrameworkContainer(Map<String, Object> config, List<BundleActivator> activators) {
        this.config = config;
        config.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, activators);
    }
}
