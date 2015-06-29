/*
 * Copyright 2013 original author or authors.
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
package org.tawja.maven.plugins.parallelexec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;
import org.tawja.maven.plugins.parallelexec.executable.ExecutableDescriptor;
import org.tawja.maven.plugins.parallelexec.executable.FileProcessDescriptor;
import org.twdata.maven.mojoexecutor.MojoExecutor;

/**
 * Common properties for all maven goals related to ParallelExec. Additionnaly
 * manage all inputs needed for the execution (scan source files).
 *
 * @author Adam DUBIEL
 * @author Jaafar BENNANI-SMIRES
 */
public abstract class BaseMavenParallelExecMojo extends AbstractMojo {

    /**
     * Exec arguments.
     */
    @Parameter
    protected String[] arguments;

    /**
     * Exec checks, used to validate environnement variables.
     */
    @Parameter
    protected Map<String, String> checks;

    /**
     * Disable the execution
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".disabled", defaultValue = "false")
    protected boolean disabled;

    /**
     * Set the thread pool size
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".threadPoolSize")
    protected int threadPoolSize = 4;

    /**
     * Map of environment variables passed to npm install.
     */
    @Parameter
    protected Map<String, String> environmentVar;

    /**
     * Extention that will be used for Windows OS (will replace unix one if any)
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".windowsExtension")
    protected String windowsExtension;

    /**
     * Tells if output files are overwritten even if they are newer than input
     * files.
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".overwriteIfNewer", defaultValue = "false")
    protected Boolean overwriteIfNewer;

    /**
     * Defines which of the included files in the source directories to exclude
     * (non by default).
     */
    @Parameter
    protected String[] excludes;
    /**
     * Name of executable in PATH, defaults to ''.
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".executable", required = true)
    protected String executable;
    /**
     * File that will contains files list managed, defaults to
     * ${project.build.directory}/parallelexec-listfiles.txt.
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".fileListFile", defaultValue = "${project.build.directory}/parallelexec-listfiles.txt")
    protected File fileListFile;

    /**
     * Defines files in the source directories to include (all .java files by
     * default).
     */
    @Parameter(required = true)
    protected String[] includes;

    protected List<FileProcessDescriptor> fileProcessDescriptors = new ArrayList<>();
    protected List<ExecutableDescriptor> executableDescriptors = new ArrayList<>();

    /**
     * Access to Maven Project object
     */
    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject mavenProject;

    /**
     * Access to Maven Session object
     */
    @Parameter(property = "session", readonly = true, required = true)
    protected MavenSession mavenSession;
    /**
     * List of additional options passed to npm when calling install.
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".options")
    protected String[] options;
    /**
     * Inject building OS name.
     */
    @Parameter(defaultValue = "${os.name}")
    protected String osName;

    /**
     * Maven 2.x uncompatibility.
     */
    @Component
    protected BuildPluginManager pluginManager;

    /**
     * Path for source files directory (from where input files are seached),
     * defaults to ${basedir}/src/main/resources.
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".sourceDirectory", defaultValue = "${basedir}/src/main/resources")
    protected String sourceDirectory;

    /**
     * Path for target files directory (where ouput files will be put), defaults
     * to ${project.build.directory}/generrated-resources.
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".targetDirectory", defaultValue = "${project.build.directory}/generated-resources")
    protected String targetDirectory;

    /**
     * Extension for target files (ouput files type)
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".targetExtension")
    protected String targetExtension;

    /**
     * Path to working directory (from where to call the executable), defaults
     * to ${project.build.directory}.
     */
    @Parameter(property = ParallelExecConstants.PROP_PREFIX + ".workingDirectory", defaultValue = "${project.build.directory}")
    protected String workingDirectory;

    protected String basedir() {
        try {
            return mavenProject.getBasedir().getCanonicalPath();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not extract basedir of project.", exception);
        }
    }

    /**
     * Main execution method. Childs should only override executeInternal()
     *
     * @throws org.apache.maven.plugin.MojoExecutionException
     * @throws org.apache.maven.plugin.MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Log log = getLog();

        log.info("ParallelExe Information :");
        log.info("     - Executable = '" + executable + "'");
        log.info("     - WorkingDirectory = '" + workingDirectory + "'");
        log.info("     - SourceDirectory = '" + sourceDirectory + "'");
        log.info("     - TargetDirectory = '" + targetDirectory + "'");
        log.info("     - TargetDExtension = '" + targetExtension + "'");

        Boolean hasCheckErrors = checkInputs();

        if (hasCheckErrors) {
            throw new MojoFailureException("Execution stopped due to check errors.");
        }

        String rootDir = basedir();

        if (sourceDirectory != null && !sourceDirectory.isEmpty()) {
            try {
                rootDir = new File(sourceDirectory).getCanonicalPath();
            } catch (IOException exception) {
                throw new IllegalStateException("Could not extract sourceDirectory of project.", exception);
            }
        }

        initializeParameters();

        if (disabled) {
            getLog().info("Execution disabled using configuration option.");
            return;
        }

        BufferedWriter writer = null;
        try {
            fileListFile.getParentFile().mkdirs();
            writer = new BufferedWriter(new FileWriter(fileListFile));
        } catch (IOException e) {
            throw new MojoExecutionException("Could not open files list '" + fileListFile + "'", e);
        }
        scanFiles(new File(rootDir), writer);
        IOUtil.close(writer);

        executeInternal();
    }

    /**
     * Check all Inputs
     *
     * @return
     */
    protected Boolean checkInputs() {
        final Log log = getLog();
        Boolean hasErrors = false;

        // User defined checks
        if (checks != null && checks.size() > 0) {
            for (Entry<String, String> entrySet : checks.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                if (value == null || value.isEmpty()) {
                    log.error("Check fails : " + key + " : Value not defined");
                    hasErrors = true;
                }
            }
        }

        return hasErrors;
    }

    /**
     * Simplified execution method. Used by childs.
     *
     * @throws org.apache.maven.plugin.MojoExecutionException
     * @throws org.apache.maven.plugin.MojoFailureException
     */
    protected abstract void executeInternal() throws MojoExecutionException, MojoFailureException;

    /**
     * Initialize parameters
     */
    private void initializeParameters() {
        // TODO : ...
    }

    /* TODO : Add in child mojos
     protected String fullTargetDirectory() {
     return basedir() + File.separator + targetDirectory;
     }
     */
    protected MojoExecutor.ExecutionEnvironment getPluginExecutionEnvironment() {
        MojoExecutor.ExecutionEnvironment environment;
        /*
         try {
         Object o = mavenSession.lookup("org.apache.maven.plugin.BuildPluginManager");
         environment = MojoExecutor.executionEnvironment(mavenProject, mavenSession, (BuildPluginManager) o);
         } catch (ComponentLookupException e) {
         environment = MojoExecutor.executionEnvironment(mavenProject, mavenSession, pluginManager);
         }
         */
        environment = MojoExecutor.executionEnvironment(mavenProject, mavenSession, pluginManager);
        return environment;
    }

    /**
     * Scans a single directory.
     *
     * @param root Directory to scan
     * @param writer Where to write the source list
     * @throws MojoExecutionException in case of IO errors
     */
    protected void scanFiles(File root, BufferedWriter writer) throws MojoExecutionException {
        final Log log = getLog();

        if (!root.exists()) {
            root.mkdirs();
        }

        log.info("Scanning source file directory '" + root + "'");

        final DirectoryScanner directoryScanner = new DirectoryScanner();
        directoryScanner.setIncludes(includes);
        directoryScanner.setExcludes(excludes);
        directoryScanner.setBasedir(root);
        directoryScanner.scan();

        for (String fileName : directoryScanner.getIncludedFiles()) {
            File inputFile = new File(root, fileName);
            FileProcessDescriptor fileProcessDesc = new FileProcessDescriptor(inputFile, sourceDirectory, targetDirectory, targetExtension);
            try {
                if (overwriteIfNewer
                        || (!overwriteIfNewer && 
                                fileProcessDesc.getInputFile().lastModified() > fileProcessDesc.getOutputFile().lastModified()
                        )
                    ) {
                    fileProcessDescriptors.add(fileProcessDesc);
                    writer.write(inputFile.getAbsolutePath());
                    writer.newLine();
                    log.info("Input file : " + inputFile);
                } else {
                    log.debug("Ignored Input file : " + inputFile);
                }
            } catch (Exception e) {
                throw new MojoExecutionException("IO error while writing files list", e);
            }
        }
    }
}
