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
package org.tawja.platform.tests.plugins.logging;

import javax.inject.Inject;
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
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
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
public class BasicLoggingTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(BasicLoggingTest.class);

    @Inject
    BundleContext context;

    //@Inject
    //private LogService logService;

    @Configuration
    public Option[] config() {

        return options(
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
                //mavenBundle("org.apache.logging.log4j", "log4j-api").versionAsInProject(),
                //mavenBundle("org.apache.logging.log4j", "log4j-core").versionAsInProject(),
                //mavenBundle("org.apache.logging.log4j.adapters", "log4j-slf4j-impl").versionAsInProject(),
                //mavenBundle("org.slf4j", "slf4j-api").versionAsInProject(),
                //mavenBundle("org.slf4j", "slf4j-ext").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.ipojo").versionAsInProject(),
                mavenBundle("org.tawja.platform", "twj-api").versionAsInProject(),
                mavenBundle("org.tawja.platform", "twj-core").versionAsInProject(),
                mavenBundle("org.tawja.platform.plugins", "twj-plugin-slf4j").versionAsInProject(),
                junitBundles()
        );
    }

    @Test
    public void checkInject() {
        //assertNotNull(context);
        //assertNotNull(helloService);
    }

    @Test
    public void checkLoggingBundle() {
        Boolean bundleFound = false;
        Boolean bundleActive = false;

        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle != null) {
                if (bundle.getSymbolicName().equals("twj-plugin-slf4j")) {
                    bundleFound = true;
                    if (bundle.getState() == Bundle.ACTIVE) {
                        bundleActive = true;
                    }
                }
            }
        }
        assertTrue(bundleFound);
        // Not active as there are multiple logservice at the same time
        //assertTrue(bundleActive);
    }

    @Test
    public void getLogServiceAsIPojo() {
        // Not injected with iPojo...
        //logService.log(LOG_INFO, "LogService is available");
    }

    @Test
    public void getLogService() {
        ServiceReference ref = context.getServiceReference(LogService.class.getName());
        if (ref != null) {
            LogService logService = (LogService) context.getService(ref);
            logService.log(LogService.LOG_INFO, " -----------  Testing OSGI logging  -------------------- ");
        } else {
            assertTrue("LogService unavailable", false);
        }
    }
}
