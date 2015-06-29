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
package org.tawja.maven.plugins.generator;

/**
 * This is a Maven Plugin front end for the FMPP. The plugin provides
 * functionality for generating code, test code, resources and test resources
 * for a maven project.
 *
 * @author Jaafar BENNANI-SMIRES
 * @author Faisal FEROZ (Initial work under MIT License)
 */
import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import fmpp.ProcessingException;
import fmpp.progresslisteners.ConsoleProgressListener;
import fmpp.setting.SettingException;
import fmpp.setting.Settings;
import fmpp.util.MiscUtil;

/**
 *
 * Generates artifacts as configured. The output is placed in configured output
 * directory.
 *
 * @goal generate
 * @phase generate-sources
 */
public class GeneratorMojo extends AbstractMojo {

    private static final String DEFAULT_ERROR_MSG = "\"%s\" is a required parameter. ";

    /**
     * Project instance, needed for attaching the build info file. Used to add
     * new source directory to the build.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     * @since 1.0
	 *
     */
    private MavenProject project;

    /**
     * Location of the output files.
     *
     * @parameter
     * default-value="${project.build.directory}/generated-sources/fmpp/"
     * @required
     */
    private File outputDirectory;

    /**
     * Location of the FreeMarker template files.
     *
     * @parameter default-value="src/main/resources/fmpp/templates/"
     * @required
     * @since 1.0
     */
    private File templateDirectory;

    /**
     * Location of the FreeMarker config file.
     *
     * @parameter default-value="src/main/resources/fmpp/config.fmpp"
     * @required
     * @since 1.0
     */
    private File cfgFile;

    public void execute() throws MojoExecutionException, MojoFailureException {

        validateParameters();

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        try {
            Settings settings = new Settings(new File("."));
            settings.set("sourceRoot", templateDirectory.getAbsolutePath());
            settings.set("outputRoot", outputDirectory.getAbsolutePath());

            settings.load(cfgFile);
            settings.addProgressListener(new ConsoleProgressListener());
            settings.execute();

            getLog().info("Done");

        } catch (SettingException e) {

            getLog().error(MiscUtil.causeMessages(e));
            throw new MojoFailureException(MiscUtil.causeMessages(e), e);

        } catch (ProcessingException e) {

            getLog().error(MiscUtil.causeMessages(e));
            throw new MojoFailureException(MiscUtil.causeMessages(e), e);
        } finally {
            // add the output directory path to the project source directories
            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
            project.addTestCompileSourceRoot(outputDirectory.getAbsolutePath());
        }
    }

    /**
     * Validates the parameters that are required by the plug-in.
     *
     * @throws MojoExecutionException Throws this exception when the data is not
     * valid.
     */
    private void validateParameters() throws MojoExecutionException {

        if (project == null) {
            throw new MojoExecutionException(String.format(DEFAULT_ERROR_MSG, "project") + "This plugin can be used only inside a project.");
        }

        if (outputDirectory == null) {
            throw new MojoExecutionException(String.format(DEFAULT_ERROR_MSG, "outputDirectory"));
        }

        if (templateDirectory == null) {
            throw new MojoExecutionException(String.format(DEFAULT_ERROR_MSG, "templateDirectory"));
        }

        if (cfgFile == null) {
            throw new MojoExecutionException(String.format(DEFAULT_ERROR_MSG, "cfgFile"));
        }
    }
}
