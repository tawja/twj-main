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
package org.tawja.maven.plugins.frontend;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.twdata.maven.mojoexecutor.MojoExecutor;

/**
 * Common properties for all maven-grunt goals.
 *
 * @author Adam Dubiel
 */
public abstract class BaseMavenGruntMojo extends AbstractMojo {

    /**
     * Path to build directory (target for grunt sources), defaults to ${basedir}/target-grunt.
     */
    @Parameter(property = "gruntBuildDirectory", defaultValue = "${basedir}/target-grunt")
    protected String gruntBuildDirectory;

    /**
     * Path to dir, where jsSourceDir is located, defaults to src/main/webapp.
     */
    @Parameter(property = "sourceDirectory", defaultValue = "src/main/webapp")
    protected String sourceDirectory;

    /**
     * Path to dir from where to copy all files that add to grunt environment - has to include package.json and Gruntfile.js, defaults to
     * "frontend".
     */
    @Parameter(property = "frontendSourceDirectory", defaultValue = "frontend")
    protected String frontendSourceDirectory;

    /**
     * Path to dir inside WAR to which grunt build artifacts will be copied, defaults to frontendSourceDirectory value.
     */
    @Parameter(property = "warTargetDirectory")
    protected String warTargetDirectory = null;

    /**
     * Name of packed node_modules TAR file, defaults to node_modules.tar.
     */
    @Parameter(property = "npmOfflineModulesFile", defaultValue = "node_modules.tar")
    protected String npmOfflineModulesFile;

    /**
     * Name of packed bower_components TAR file, defaults to bower_components.tar.
     */
    @Parameter(property = "bowerOfflineComponentsFile", defaultValue = "bower_components.tar")
    protected String bowerOfflineComponentsFile;

    @Parameter(property = "disabled", defaultValue = "false")
    private boolean disabled;

    /**
     * Path to packed node_modules TAR file directory relative to basedir, defaults to target-grunt directory (ex target-grunt/).
     */
    @Parameter(property = "npmOfflineModulesFilePath", defaultValue = "target-grunt")
    protected String npmOfflineModulesFilePath;

    /**
     * Path to packed bower_components TAR file directory relative to basedir, defaults to target-grunt directory (ex target-grunt/).
     */
    @Parameter(property = "bowerOfflineComponentsFilePath", defaultValue = "target-grunt")
    protected String bowerOfflineComponentsFilePath;


    @Parameter(property = "project", readonly = true, required = true)
    private MavenProject mavenProject;

    @Parameter(property = "session", readonly = true, required = true)
    private MavenSession mavenSession;

    /**
     * Maven 2.x uncompatibility.
     */
    @Component
    protected BuildPluginManager pluginManager;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        initializeParameters();
        if (!disabled) {
            executeInternal();
        } else {
            getLog().info("Execution disabled using configuration option.");
        }
    }

    protected abstract void executeInternal() throws MojoExecutionException, MojoFailureException;

    private void initializeParameters() {
        if (warTargetDirectory == null) {
            warTargetDirectory = frontendSourceDirectory;
        }
    }

    protected String basedir() {
        try {
            return mavenProject.getBasedir().getCanonicalPath();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not extract basedir of project.", exception);
        }
    }

    protected String target() {
        return mavenProject.getBuild().getDirectory();
    }

    protected String fullFrontendSourceDirectory() {
        return basedir() + File.separator + sourceDirectory + File.separator + frontendSourceDirectory;
    }

    protected String relativeFrontendSourceDirectory() {
        return sourceDirectory + File.separator + frontendSourceDirectory;
    }

    protected MojoExecutor.ExecutionEnvironment pluginExecutionEnvironment() {
        MojoExecutor.ExecutionEnvironment pluginEnv = getPluginExecutionEnvironment();
        return pluginEnv;
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
}
