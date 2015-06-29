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

import javax.inject.Inject;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tawja.platform.tests.utils.TestBase;

/**
 * Tests the given bundle with JUnit and Pax Exam.
 *
 * @author Jaafar BENNANI-SMIRES
 */
@RunWith(PaxExam.class)
//@ExamReactorStrategy(PerClass.class)
public class BasicCoreTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(BasicCoreTest.class);

    @Inject
    BundleContext context;

    @Configuration
    public Option[] config() {

        return options(
                cleanCaches(),
                ipojoBundles(),
                twjBundles(),
                junitBundles(),

                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
                //mavenBundle("org.apache.logging.log4j", "log4j-api", "2.1"),
                //mavenBundle("org.apache.logging.log4j", "log4j-core", "2.1"),
                //mavenBundle("org.apache.logging.log4j.adapters", "log4j-slf4j-impl", "2.0-beta4"),
                //mavenBundle("org.slf4j", "slf4j-api", "1.7.10"),
                //mavenBundle("org.slf4j", "slf4j-ext", "1.7.10"),
                mavenBundle("org.tawja.platform", "twj-api", "1.0.0-SNAPSHOT"),
                mavenBundle("org.tawja.platform", "twj-core", "1.0.0-SNAPSHOT"),
                /*mavenBundle("org.tawja.platform", "twj-core", "1.0.0-SNAPSHOT"),*/
                mavenBundle("org.apache.felix", "org.apache.felix.ipojo", "1.12.1"),
                junitBundles()
        );
    }

    @Test
    public void checkInject() {
        //assertNotNull(context);
        //assertNotNull(helloService);
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
        //assertEquals("hello test", helloService.sayHello("test"));
    }
}
