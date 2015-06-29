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
package org.tawja.platform.tests.console;

import javax.inject.Inject;
import static org.junit.Assert.assertNotNull;
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
public class BasicConsoleTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(BasicConsoleTest.class);

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
                //mavenBundle("org.apache.logging.log4j").versionAsInProject(),
                //mavenBundle("org.apache.logging.log4j", "log4j-core").versionAsInProject(),
                //mavenBundle("org.apache.logging.log4j.adapters", "log4j-slf4j-impl").versionAsInProject(),
                //mavenBundle("org.slf4j", "slf4j-api").versionAsInProject(),
                //mavenBundle("org.slf4j", "slf4j-ext").versionAsInProject(),
                mavenBundle("org.tawja.platform", "twj-api").versionAsInProject(),
                mavenBundle("org.tawja.platform", "twj-core").versionAsInProject(),
                /*mavenBundle("org.tawja", "twj-core"),*/
                mavenBundle("org.apache.felix", "org.apache.felix.ipojo").versionAsInProject(),
                junitBundles()
        );
    }

    @Test
    public void checkInject() {
        assertNotNull(context);
    }

    @Test
    public void checkHelloBundle() {
    }
}
