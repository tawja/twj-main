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
package org.tawja.platform.tests.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import javax.inject.Inject;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import org.ops4j.pax.exam.options.CompositeOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.tawja.platform.api.core.Hello;
import org.tawja.platform.tests.utils.TestBase;

/**
 * Tests the given bundle with JUnit and Pax Exam.
 *
 * @author Jaafar BENNANI-SMIRES
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class AdvancedCoreTestIT extends TestBase {

    @Inject
    BundleContext context;

    @Inject
    private Hello helloService;

    @Configuration
    public Option[] config() {

        // Reduce log level.
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        return options(
                cleanCaches(),
                ipojoBundles(),
                twjBundles(),
                junitBundles(),
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
                mavenBundle("org.tawja.platform", "twj-api").versionAsInProject(),
                mavenBundle("org.tawja.platform", "twj-core").versionAsInProject()
        );
    }

    @Test
    public void checkInject() {
        assertNotNull("Null Bundle Context", context);
        assertNotNull("Null Hello Service", helloService);
    }

    @Test
    public void checkHelloBundle() {
        Boolean bundleHelloFound = false;
        Boolean bundleHelloActive = false;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle != null) {
                if ("twj-api".equals(bundle.getSymbolicName())) {
                    bundleHelloFound = true;
                    if (bundle.getState() == Bundle.ACTIVE) {
                        bundleHelloActive = true;
                    }
                }
            }
        }
        assertTrue(bundleHelloFound);
        assertTrue(bundleHelloActive);
    }

    @Test
    public void getHelloService() {
        assertEquals("Hello test", helloService.sayHello("test"));
    }
}
