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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import javax.inject.Inject;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
//import org.osgi.service.command.CommandProcessor;
//import org.osgi.service.command.CommandSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.slf4j.LoggerFactory;
import org.tawja.platform.tests.utils.TestBase;

/**
 * Tests the given bundle with JUnit and Pax Exam.
 *
 * @author Jaafar BENNANI-SMIRES
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class AdvancedRemoteShellTestIT extends TestBase {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BasicCoreTest.class);
    private static long COMMAND_TIMEOUT = 10000;

    @Inject
    BundleContext context;

    @Inject
    CommandProcessor commandProcessor;

    @ProbeBuilder
    public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {
        //probe.setHeader(Constants.DYNAMICIMPORT_PACKAGE, "*;status=provisional");
        probe.setHeader(Constants.DYNAMICIMPORT_PACKAGE, "*,org.apache.felix.service.*;status=provisional");
        return probe;
    }

    @Configuration
    public Option[] config() {
        return options(
                cleanCaches(),
                ipojoBundles(),
                twjBundles(),
                junitBundles(),

                // PAX Exam Time out
                systemProperty("pax.exam.service.timeout").value("160000"),
                // Force iPOJO to process metadata.xml synchronously otherwise PaxExam stops too early
                systemProperty("ipojo.processing.synchronous").value("true"),
                // Change Log Level
                systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO"),
                // Provisionning
                mavenBundle("org.ops4j.pax.logging", "pax-logging-service").versionAsInProject(),
                mavenBundle("org.ops4j.pax.logging", "pax-logging-api").versionAsInProject(),
                /*
                 mavenBundle("ch.qos.logback", "logback-core").versionAsInProject(),
                 mavenBundle("ch.qos.logback", "logback-classic").versionAsInProject(),
                 */
                mavenBundle("org.apache.felix", "org.apache.felix.dependencymanager").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.dependencymanager.runtime").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.ipojo").versionAsInProject(),
                mavenBundle("org.tawja.platform", "twj-api").versionAsInProject(),
                mavenBundle("org.tawja.platform", "twj-core").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.shell").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.shell.remote").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.gogo.runtime").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.gogo.shell").versionAsInProject(),
                mavenBundle("org.apache.felix", "org.apache.felix.gogo.command").versionAsInProject(),
                // Test Framework Provisionning
                junitBundles()
        );
    }

    @Test
    public void checkInject() {
        /*
         CommandProcessor commandProcessor = null;
         ServiceReference ref = null;
         try {
         ref = context.getServiceReference(CommandProcessor.class);
         } catch (Exception ex) {
         logger.error("Service CommandProcessor Unavaillable.", ex);
         }
         if (ref != null) {
         commandProcessor = (CommandProcessor) context.getService(ref);
         }
         */
        assertNotNull(context);
        assertNotNull(commandProcessor);
    }

    @Test
    public void checkAllBundles() {
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
    public void checkCommands() {
        logger.info(executeCommands("help"));
    }

    protected String executeCommands(final String... commands) {
        String response;
        /*
         CommandProcessor commandProcessor = null;

         ServiceReference ref = context.getServiceReference(CommandProcessor.class);
         if (ref != null) {
         commandProcessor = (CommandProcessor) context.getService(ref);
         }
         */
        final ExecutorService executor = Executors.newFixedThreadPool(5);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final PrintStream printStream = new PrintStream(byteArrayOutputStream);
        final CommandSession commandSession = commandProcessor.createSession(System.in, printStream, System.err);
        FutureTask<String> commandFuture = new FutureTask<String>(
                new Callable<String>() {
                    public String call() {
                        try {
                            for (String command : commands) {
                                System.err.println(command);
                                commandSession.execute(command);
                            }
                        } catch (Exception e) {
                            e.printStackTrace(System.err);
                        }
                        return byteArrayOutputStream.toString();
                    }
                });

        try {
            executor.submit(commandFuture);
            response = commandFuture.get(COMMAND_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            response = "SHELL COMMAND TIMED OUT: ";
        }

        return response;
    }
}
