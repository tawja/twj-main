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
package org.tawja.platform.core.internal;

import java.util.HashMap;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.tawja.platform.core.TwjAbstractBundleActivator;

/**
 * Activator for <code>twj-core</code> bundle.
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class Activator extends TwjAbstractBundleActivator {

    /**
     * Application startup arguments.
     */
    private String[] arguments;

    /**
     * Application configuration parameters.
     */
    private Map<String, String> config;
    /**
     * System bundle context.
     */
    private BundleContext context;

    /**
     * Creates new instance.
     */
    public Activator() {
        arguments = new String[0];
        config = new HashMap<>();
    }

    /**
     * @return the arguments
     */
    public String[] getArguments() {
        return arguments;
    }

    /**
     * Set arguments that application was started with.
     *
     * @param arguments application arguments
     */
    public void setArguments(String[] arguments) {
        this.arguments = new String[arguments.length];
        System.arraycopy(arguments, 0, this.arguments, 0, arguments.length);
    }

    /**
     * @return the config
     */
    public Map<String, String> getConfig() {
        return config;
    }

    /**
     * Set application configuration parameters.
     *
     * @param config configuration
     */
    public void setConfig(Map<String, Object> config) {
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (entry.getValue() != null) {
                this.config.put(entry.getKey(), entry.getValue().toString());
            } else {
                this.config.put(entry.getKey(), "");
            }

        }
    }

    /**
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     *
     * @param context bundle context
     */
    @Override
    public void start(BundleContext context) {
        this.context = context;
    }

    /**
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     *
     * @param context bundle context
     */
    @Override
    public void stop(BundleContext context) {

    }
}
