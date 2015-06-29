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
package org.tawja.platform.core;

import org.tawja.platform.core.internal.Activator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.tawja.platform.api.TwjApplicationStarter;
import org.tawja.platform.api.core.TwjCoreConstants;
import org.tawja.platform.api.core.TwjCoreProperties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class TwjApplication {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TwjApplication.class.getName());

    /**
     * Home directory
     */
    private String applicationHomeDir = ".";

    /**
     * Base directory
     */
    private String applicationBaseDir = ".";

    /**
     * Application name
     */
    private String applicationName = TwjCoreConstants.APPLICATION_NAME;

    /**
     * Context name
     */
    private String contextName = TwjCoreConstants.APPLICATION_CONTEXT_NAME;

    /**
     * Instance name
     */
    private String instanceName = TwjCoreConstants.APPLICATION_INSTANCE_NAME;

    /**
     * Bundles directory
     */
    private String bundlesDirName = TwjCoreConstants.BUNDLES_DIR_NAME;

    /**
     * Configuration directory
     */
    private String bundlesConfigDirName = TwjCoreConstants.BUNDLES_CONFIG_DIR_NAME;

    /**
     * Indicate whether to force clearing bundle cache directory at startup
     */
    private Boolean forceClearCache = TwjCoreConstants.DEFAULT_CLEAR_CACHE;

    /**
     * Indicate whether to force working in current directory (used for dev and
     * archive application copy)
     */
    private Boolean forceUseCurrentDir = TwjCoreConstants.DEFAULT_FORCE_CURRDIR;

    /**
     * Configuration map.
     */
    private Map<String, Object> config;

    /**
     * Framework instance.
     */
    private Framework framework;

    /**
     * Application starter context (responsibleof the osgi container type)
     */
    private TwjApplicationStarter starter;

    public TwjApplication(TwjApplicationStarter starter) {
        this.starter = starter;
        config = new HashMap<>();
    }

    public void copySystemProperties(Map configProps) {
        for (Enumeration e = System.getProperties().propertyNames();
                e.hasMoreElements();) {
            String key = (String) e.nextElement();
            if (key.startsWith("tawja.")
                    || key.startsWith("felix.")
                    || key.startsWith("karaf.")
                    || key.startsWith("org.osgi.framework.")) {
                configProps.put(key, System.getProperty(key));
            }
        }
    }

    /**
     *
     * @return the applicationName
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * @return the applicationBaseDir
     */
    public String getApplicationBaseDir() {
        return applicationBaseDir;
    }

    /**
     *
     * @return the contextName
     */
    public String getContextName() {
        return contextName;
    }

    /**
     * @return the forceClearCache
     */
    public Boolean getForceClearCache() {
        return forceClearCache;
    }

    /**
     * @return the forceUseCurrentDir
     */
    public Boolean getForceUseCurrentDir() {
        return forceUseCurrentDir;
    }

    /**
     * @return the applicationHomeDir
     */
    public String getApplicationHomeDir() {
        return applicationHomeDir;
    }

    /**
     *
     * @return the instanceName
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     *
     * @return the bundleDirName
     */
    public String getBundlesDirName() {
        return bundlesDirName;
    }

    /**
     * @return the bundlesConfigDirName
     */
    public String getBundlesConfigDirName() {
        return bundlesConfigDirName;
    }

    /**
     * @return the framework
     */
    public Framework getFramework() {
        return framework;
    }

    /**
     * Initializes OSGI framework. This method will ...
     */
    public void init() {

        // Initialize main application arguments
        initFromArgs();

        // Load system properties.
        loadSystemProperties();

        // Read configuration properties.
        Map<String, String> configProps = loadConfigProperties();
        // If no configuration properties were found, then create
        // an empty properties object.
        if (configProps == null) {
            System.err.println("No " + TwjCoreConstants.CONFIG_PROPERTIES_FILE_NAME + " found.");
            configProps = new HashMap<String, String>();
        }

        // Copy framework properties from the system properties.
        copySystemProperties(configProps);

        //notifyPreloader(new LoadNotification(0, LoadNotification.Operation.INIT, ""));
        config = new HashMap<>();
        // check for external configuration properties
        loadExternalConfiguration(config);

        // read  packages that need to be exported in system bundle
        String extraPackagesList = null;
        try {
            extraPackagesList = loadSystemBundleExtraPackagesDefinition();
        } catch (IOException ex) {
            System.err.println("Extra packages list file not found.");
        }

        if (extraPackagesList != null && !extraPackagesList.isEmpty()) {
            config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, extraPackagesList);
        }

        // clear bundle cache if needed
        if (getForceClearCache() || "true".equals(System.getProperty(TwjCoreProperties.CLEAR_CACHE_PROP))) {
            config.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
        }

        defineWorkingDirectories();

        // Save working directories and base properties
        config.put(TwjCoreProperties.HOME_DIR_PROP, getApplicationHomeDir());
        config.put(TwjCoreProperties.BASE_DIR_PROP, getApplicationBaseDir());
        config.put(TwjCoreProperties.BUNDLES_DIR_PROP, getApplicationHomeDir() + File.separator + getBundlesDirName());
        config.put(TwjCoreProperties.BUNDLES_CONFIG_DIR_PROP, getApplicationHomeDir() + File.separator + getBundlesConfigDirName());
        config.put(Constants.FRAMEWORK_STORAGE, getApplicationHomeDir() + File.separator + TwjCoreConstants.BUNDLES_CACHE_DIR_NAME);
    }

    public void defineWorkingDirectories() {
        String userHomeDir = System.getProperty("user.home");
        String currentDir = System.getProperty("user.dir");

        // Define working directories to current directories by default
        // For platforms where application are bundled (android), home has no sens as all the app is packaged
        // Even for platforms where application are bundled, 'user.dir' is defined
        String homeDir = currentDir;
        String baseDir = currentDir;

        // Search MacOS Bundle directory
        String macosBundleDir = "";
        // If we are running inside Mac OS X bundle
        if (currentDir.endsWith(File.separator + "Contents" + File.separator + "MacOS")) {
            String[] parts = currentDir.split("/");
            for (int i = parts.length - 1; i >= 0; i--) {
                if (parts[i].endsWith(".app")) {
                    macosBundleDir = parts[i].substring(0, parts[i].indexOf(".app"));
                    break;
                }
            }
        }

        // Check MacOS Bundle
        if (macosBundleDir != null && !macosBundleDir.isEmpty()) {
            homeDir = macosBundleDir;
            baseDir = macosBundleDir;
        }

        String homeDirEnv = System.getProperty(TwjCoreConstants.APPLICATION_HOME_VAR);
        if (!forceUseCurrentDir && homeDirEnv != null && !homeDirEnv.isEmpty()) {
            homeDir = homeDirEnv;
        }

        String baseDirEnv = System.getProperty(TwjCoreConstants.APPLICATION_BASE_VAR);
        if (!forceUseCurrentDir && baseDirEnv != null && !baseDirEnv.isEmpty()) {
            baseDir = baseDirEnv;
        }

        Boolean homeDirDefined = false;

        // Check current directory containning the main container jar
        File jarContainerFile = new File(currentDir, TwjCoreConstants.MAIN_JAR_CONTAINER_NAME);
        if (!homeDirDefined && (jarContainerFile.exists())) {
            homeDir = currentDir;
            homeDirDefined = true;
        }

        String absoluteHomeDir = new File(homeDir).getAbsolutePath();
        try {
            absoluteHomeDir = new File(homeDir).getCanonicalPath();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        applicationHomeDir = absoluteHomeDir;

        String fullBaseDir = baseDir
                + File.separator + TwjCoreConstants.STORE_PREFIX + getApplicationName()
                + File.separator + getContextName() + File.separator + getInstanceName();
        String absoluteBaseDir = new File(fullBaseDir).getAbsolutePath();
        try {
            absoluteBaseDir = new File(fullBaseDir).getCanonicalPath();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        applicationHomeDir = absoluteBaseDir;
    }

    /**
     * <p>
     * Loads the configuration properties in the configuration property file
     * associated with the framework installation; these properties are
     * accessible to the framework and to bundles and are intended for
     * configuration purposes. By default, the configuration property file is
     * located in the <tt>conf/</tt> directory of the Felix installation
     * directory and is called "<tt>config.properties</tt>". The installation
     * directory of Felix is assumed to be the parent directory of the
     * <tt>felix.jar</tt> file as found on the system class path property. The
     * precise file from which to load configuration properties can be set by
     * initializing the "<tt>felix.config.properties</tt>" system property to an
     * arbitrary URL.
     * </p>
     *
     * @return A <tt>Properties</tt> instance or <tt>null</tt> if there was an
     * error.
     *
     */
    public Map<String, String> loadConfigProperties() {
        // The config properties file is either specified by a system
        // property or it is in the conf/ directory of the Felix
        // installation directory.  Try to load it from one of these
        // places.

        // See if the property URL was specified as a property.
        URL propURL = null;
        String custom = System.getProperty(TwjCoreProperties.CONFIG_PROPERTIES_PROP);
        if (custom != null) {
            try {
                propURL = new URL(custom);
            } catch (MalformedURLException ex) {
                System.err.print("Main: " + ex);
                return null;
            }
        } else {
            // Determine where the configuration directory is by figuring
            // out where felix.jar is located on the system class path.
            File confDir = null;
            String classpath = System.getProperty("java.class.path");
            int index = classpath.toLowerCase().indexOf("felix.jar");
            int start = classpath.lastIndexOf(File.pathSeparator, index) + 1;
            if (index >= start) {
                // Get the path of the felix.jar file.
                String jarLocation = classpath.substring(start, index);
                // Calculate the conf directory based on the parent
                // directory of the felix.jar directory.
                confDir = new File(
                        new File(new File(jarLocation).getAbsolutePath()).getParent(),
                        getBundlesConfigDirName());
            } else {
                // Can't figure it out so use the current directory as default.
                confDir = new File(System.getProperty("user.dir"), getBundlesConfigDirName());
            }

            try {
                propURL = new File(confDir, TwjCoreConstants.CONFIG_PROPERTIES_FILE_NAME).toURI().toURL();
            } catch (MalformedURLException ex) {
                System.err.print("Main: " + ex);
                return null;
            }
        }

        // Read the properties file.
        Properties props = new Properties();
        InputStream is = null;
        try {
            // Try to load config.properties.
            is = propURL.openConnection().getInputStream();
            props.load(is);
            is.close();
        } catch (Exception ex) {
            // Try to close input stream if we have one.
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex2) {
                // Nothing we can do.
            }

            return null;
        }

        // Perform variable substitution for system properties and
        // convert to dictionary.
        Map<String, String> map = new HashMap<String, String>();
        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
            String name = (String) e.nextElement();
            map.put(name,
                    Utils.substVars(props.getProperty(name), name, null, props));
        }

        return map;
    }

    /**
     * <p>
     * Loads the properties in the system property file associated with the
     * framework installation into <tt>System.setProperty()</tt>. These
     * properties are not directly used by the framework in anyway. By default,
     * the system property file is located in the <tt>config/</tt> directory of
     * the Felix installation directory and is called
     * "<tt>system.properties</tt>". The installation directory of Felix is
     * assumed to be the parent directory of the <tt>felix.jar</tt> file as
     * found on the system class path property. The precise file from which to
     * load system properties can be set by initializing the
     * "<tt>felix.system.properties</tt>" system property to an arbitrary URL.
     * </p>
     *
     */
    public void loadSystemProperties() {
        // The system properties file is either specified by a system
        // property or it is in the same directory as the Felix JAR file.
        // Try to load it from one of these places.

        // See if the property URL was specified as a property.
        URL propURL = null;
        String custom = System.getProperty(TwjCoreProperties.SYSTEM_PROPERTIES_PROP);
        if (custom != null) {
            try {
                propURL = new URL(custom);
            } catch (MalformedURLException ex) {
                System.err.print("Main: " + ex);
                return;
            }
        } else {
            // Determine where the configuration directory is by figuring
            // out where felix.jar is located on the system class path.
            File confDir = null;
            String classpath = System.getProperty("java.class.path");
            int index = classpath.toLowerCase().indexOf("felix.jar");
            int start = classpath.lastIndexOf(File.pathSeparator, index) + 1;
            if (index >= start) {
                // Get the path of the felix.jar file.
                String jarLocation = classpath.substring(start, index);
                // Calculate the conf directory based on the parent
                // directory of the felix.jar directory.
                confDir = new File(
                        new File(new File(jarLocation).getAbsolutePath()).getParent(),
                        getBundlesConfigDirName());
            } else {
                // Can't figure it out so use the current directory as default.
                confDir = new File(System.getProperty("user.dir"), getBundlesConfigDirName());
            }

            try {
                propURL = new File(confDir, TwjCoreConstants.SYSTEM_PROPERTIES_FILE_NAME).toURI().toURL();
            } catch (MalformedURLException ex) {
                System.err.print("Main: " + ex);
                return;
            }
        }

        // Read the properties file.
        Properties props = new Properties();
        InputStream is = null;
        try {
            is = propURL.openConnection().getInputStream();
            props.load(is);
            is.close();
        } catch (FileNotFoundException ex) {
            // Ignore file not found.
        } catch (Exception ex) {
            System.err.println(
                    "Main: Error loading system properties from " + propURL);
            System.err.println("Main: " + ex);
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex2) {
                // Nothing we can do.
            }
            return;
        }

        // Perform variable substitution on specified properties.
        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
            String name = (String) e.nextElement();
            System.setProperty(name,
                    Utils.substVars(props.getProperty(name), name, null, null));
        }
    }

    /**
     * Starts OSGI framework. This method will spawn new thread which will load
     * the framework and track loading progress while it is starting. During
     * startup, bundles from specified directory will be installed and started.
     */
    public void start() {
        Thread starterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LOG.info("Starting application...");
                    List<BundleActivator> activators = new ArrayList<>();
                    Activator activator = new Activator();
                    //activator.setWindowService(new JFXWindowServiceImpl(primaryStage));
                    //List<String> args = getParameters().getRaw();
                    //activator.setArguments(args.toArray(new String[args.size()]));
                    activator.setConfig(config);

                    activators.add(activator);

                    starter.configureOsgiFrameworkContainer(config, activators);

                    try {
                        framework = starter.getOsgiFrameworkContainer();
                        framework.start();

                        File bundleDir = new File(getBundlesDirName());
                        File[] files = new File[0];
                        if (bundleDir.exists()) {
                            files = bundleDir.listFiles(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String name) {
                                    return name.endsWith(TwjCoreConstants.JAR_EXTENSION);
                                }
                            });
                        }

                        int total = files.length * 2;
                        int current = 0;
                        for (File f : files) {
                            LOG.info("Bundle Installing : " + (current + 1) + " : " + f.getName());
                            framework.getBundleContext().installBundle(f.toURI().toURL().toExternalForm());
                            current++;
                            //notifyPreloader(new LoadNotification(current / total, LoadNotification.Operation.INSTALL, f.getName()));
                        }
                        for (Bundle b : framework.getBundleContext().getBundles()) {
                            if (b.getState() != Bundle.ACTIVE) {
                                // if this is fragment bundle, skip it
                                // we check for Fragment-Host header
                                if (b.getHeaders().get(Constants.FRAGMENT_HOST) != null) {
                                    continue;
                                }
                                LOG.info("Bundle Starting : " + b.getBundleId() + " : " + b.getSymbolicName());
                                b.start();
                            }
                            current++;
                            //notifyPreloader(new LoadNotification(current / total, LoadNotification.Operation.START, b.getSymbolicName()));
                        }

                        //ready.setValue(Boolean.TRUE);
                        LOG.info("...Application READY.");
                    } catch (BundleException | MalformedURLException bex) {
                        bex.printStackTrace(System.err);
                        stop();
                    }

                    //notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
                    //return null;
                } catch (Exception ex) {
                    System.err.println("ERROR Uncatched Exception : " + ex.getMessage());
                    ex.printStackTrace(System.err);
                    stop();
                }
            }
        }, "TwjStarterThread");
        starterThread.start();
    }

    public void stop() {
        try {
            if (framework != null) {
                Bundle[] bundles = framework.getBundleContext().getBundles();
                for (Bundle b : bundles) {
                    if (b.getState() == Bundle.ACTIVE) {
                        try {
                            b.stop();
                        } catch (BundleException be) {
                            be.printStackTrace(System.err);
                            System.exit(2);
                        }
                    }
                }

                try {
                    framework.stop();
                } catch (BundleException be) {
                    be.printStackTrace(System.err);
                    System.exit(2);
                }
                try {
                    framework.waitForStop(TwjCoreConstants.CONF_STOP_TIMEOUT);
                } catch (InterruptedException ie) {
                    ie.printStackTrace(System.err);
                    System.exit(2);
                }
            }
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("ERROR Uncatched Exception : " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(2);
        }
    }

    /**
     * Check if external configuration file exists. This file is assumed to be
     * named <code>config.properties</code>, and should be present in work
     * directory.
     *
     * @return empty {@code Properties} object if file does not exist, or loaded
     * properties if it does
     * @throws IOException if an error occurs
     */
    private Properties checkForConfigOptions() throws IOException {
        Properties props = new Properties();
        File file = new File(getBundlesConfigDirName(), TwjCoreConstants.CONFIG_PROPERTIES_FILE_NAME);

        if (file.exists()) {
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
                if (file.exists()) {
                    props.load(reader);
                }
            }
        }

        return props;
    }

    /**
     * Finds a list of packages that should be exported from system bundle.
     * These packages should be specified in a file called
     * <code>extra-packages</code> and should be located in default package.
     * File encoding must be <code>UTF-8</code>
     *
     * @return list of comma separated packages
     * @throws IOException if an error occurs
     */
    private String loadSystemBundleExtraPackagesDefinition() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(TwjCoreConstants.EXTRA_PACKAGES_FILE_NAME), "UTF-8"));) {

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private void initFromArgs() {
        String[] args = starter.getArgs();
        boolean hasErrors = false;

        // Look for bundle directory and/or cache directory.
        // We support at most one argument, which is the bundle
        // cache directory.
        boolean expectBundlesDir = false;
        boolean expectConfigDir = false;
        boolean expectClearCache = false;
        boolean expectCurrentDirUse = false;
        boolean expectAppName = false;
        boolean expectAppContextName = false;
        boolean expectAppInstanceName = false;

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(TwjCoreConstants.SWITCH_BUNDLES_DIR)) {
                    expectBundlesDir = true;
                } else if (args[i].equals(TwjCoreConstants.SWITCH_CONFIG_DIR)) {
                    expectConfigDir = true;
                } else if (args[i].equals(TwjCoreConstants.SWITCH_CLEAR_CACHES)) {
                    expectClearCache = true;
                } else if (args[i].equals(TwjCoreConstants.SWITCH_FORCE_CURRDIR)) {
                    expectCurrentDirUse = true;
                } else if (args[i].equals(TwjCoreConstants.SWITCH_APPLICATION_NAME)) {
                    expectAppName = true;
                } else if (args[i].equals(TwjCoreConstants.SWITCH_CONTEXT_NAME)) {
                    expectAppContextName = true;
                } else if (args[i].equals(TwjCoreConstants.SWITCH_INSTANCE_NAME)) {
                    expectAppInstanceName = true;
                } else if (expectBundlesDir) {
                    bundlesDirName = args[i];
                    expectBundlesDir = false;
                } else if (expectConfigDir) {
                    bundlesConfigDirName = args[i];
                    expectConfigDir = false;
                } else if (expectClearCache) {
                    forceClearCache = Boolean.parseBoolean(args[i]);
                    expectClearCache = false;
                } else if (expectCurrentDirUse) {
                    forceUseCurrentDir = Boolean.parseBoolean(args[i]);
                    expectCurrentDirUse = false;
                } else if (expectAppName) {
                    applicationName = args[i];
                    expectAppName = false;
                } else if (expectAppContextName) {
                    contextName = args[i];
                    expectAppContextName = false;
                } else if (expectAppInstanceName) {
                    instanceName = args[i];
                    expectAppInstanceName = false;
                } else {
                    LOG.severe("Unexpected argument : " + args[i]);
                }
            }

            if (hasErrors || (args.length > 3)) {
                LOG.info(
                        "Usage: " + System.lineSeparator()
                        + "     [" + TwjCoreConstants.SWITCH_APPLICATION_NAME + " <application-name>] " + System.lineSeparator()
                        + "     [" + TwjCoreConstants.SWITCH_CONTEXT_NAME + " <context-name>] " + System.lineSeparator()
                        + "     [" + TwjCoreConstants.SWITCH_INSTANCE_NAME + " <instance-name>] " + System.lineSeparator()
                        + "     [" + TwjCoreConstants.SWITCH_CONFIG_DIR + " <bundles-config-dir>] " + System.lineSeparator()
                        + "     [" + TwjCoreConstants.SWITCH_BUNDLES_DIR + " <bundles-deploy-dir>] " + System.lineSeparator()
                );
                System.exit(0);
            }
        }
    }

    /**
     * Loads configuration properties from external file. This file should be
     * named <code>configMap.properties</code> and should be in working
     * directory.
     *
     * @param configMap configuration map
     */
    private void loadExternalConfiguration(Map<String, Object> configMap) {
        try {
            Properties props = checkForConfigOptions();
            if (!props.isEmpty()) {
                Enumeration<?> names = props.propertyNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement().toString();
                    configMap.put(name, props.getProperty(name));
                }
            }
        } catch (IOException ex) {
            System.err.println("Could not read configuration properties: " + ex.getMessage());
        }
    }
}
