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
 * @author Jaafar BENNANI-SMIRES
 * @author Faisal FEROZ (Initial work under MIT License)
 */

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.junit.Test;

public class GeneratorMojoForJenkinContinuousTest extends AbstractMojoTestCase {

    private final File outputDirectory = new File("generated-sources/jenkins-continuous/");
    private final File templateDirectory = new File("../../src/test/resources/jenkins-continuous/templates/");
    private final File cfgFile = new File("../../src/test/resources/jenkins-continuous/");

    @Override
    protected void setUp() throws Exception {
        cleanDirectory(outputDirectory);
        super.setUp();
    }

    @Test
    public void testCodeGeneration() throws Exception {

        Mojo mojo = getConfiguredMojo();
        mojo.execute();

        assertTrue(outputDirectory.exists());
        List<String> files = Arrays.asList(outputDirectory.list());
        assertTrue(files.size() > 0);
        //assertTrue("Output file not created.", files.contains("Tawja"));
    }

    /**
     * @return The configured Mojo for testing.
     * @throws Exception
     */
    private Mojo getConfiguredMojo() throws Exception {

        File pluginXml = new File(getBasedir(), "src/test/resources/jenkins-continuous/plugin-config.xml");
        Mojo mojo = lookupMojo("generate", pluginXml);

        setVariableValueToObject(mojo, "project", new MavenProjectStub());
        setVariableValueToObject(mojo, "outputDirectory", outputDirectory);
        setVariableValueToObject(mojo, "templateDirectory", templateDirectory);
        setVariableValueToObject(mojo, "cfgFile", cfgFile);

        return mojo;
    }

    /**
     * cleans the output directory with all the artifacts created as a result of
     * mojo execution
     */
    private void cleanDirectory(final File directory) {
        if (directory != null) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        cleanDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
