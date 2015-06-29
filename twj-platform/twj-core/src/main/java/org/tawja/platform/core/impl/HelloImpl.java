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
package org.tawja.platform.core.impl;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tawja.platform.api.core.Hello;
import org.tawja.platform.core.TwjApplication;

/**
 * OSGi iPOJO component that implements the Hello interface.
 *
 * @author Jaafar BENNANI-SMIRES
 */
@Component
@Provides
@Instantiate
public class HelloImpl implements Hello {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TwjApplication.class.getName());

    @Validate
    public void start() {
        LOG.info("Start component");

    }

    @Invalidate
    public void stop() {
        LOG.info("Stop component");

    }

    public String sayHello(String name) {
        return "Hello " + name;
    }
}
