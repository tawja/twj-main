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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.tawja.maven.plugins.parallelexec.executable.Executable;
import org.tawja.maven.plugins.parallelexec.executable.ExecutableDescriptor;
import org.tawja.maven.plugins.parallelexec.executable.FileProcessDescriptor;
import org.tawja.maven.plugins.parallelexec.executable.WorkerThread;
import org.twdata.maven.mojoexecutor.MojoExecutor;

//import org.apache.commons.exec.OS;
/**
 * Abstract mojo which uses MojoExecutor to execute exec-maven-plugin, which in
 * turn executes system command - command line only.
 *
 * Compatible with Windows via
 * <pre>cmd /C</pre>.
 *
 * @author Adam DUBIEL
 * @author Jaafar BENNANI-SMIRES
 */
public abstract class AbstractExecutableMojo extends BaseMavenParallelExecMojo {

    /**
     * Version of maven exec plugin to use (defaults to 1.2.1).
     */
    @Parameter(property = "execMavenPluginVersion", defaultValue = ParallelExecConstants.EXEC_MAVEN_VERSION)
    protected String execMavenPluginVersion;

    /**
     * Specialize execute method. Here all input parameters should be defined
     *
     * @throws org.apache.maven.plugin.MojoExecutionException
     * @throws org.apache.maven.plugin.MojoFailureException
     */
    @Override
    public void executeInternal() throws MojoExecutionException, MojoFailureException {
        initializeExecutableDescriptors();
        runExecutablesForAllFile();
    }

    /**
     * Return executable form maven exec
     * <pre>executable element</pre>, return executable name for *nix OS, not
     * Windows!
     */
    protected abstract void initializeExecutableDescriptors();

    private void runExecutablesForAllFile() {
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        MojoExecutor.ExecutionEnvironment pluginEnv = getPluginExecutionEnvironment();
        
        int i = 0;
        for (ExecutableDescriptor execDesc : executableDescriptors) {
            for (FileProcessDescriptor fileDesc : fileProcessDescriptors) {
                //runExecutableForFile();
                Runnable worker = new WorkerThread(ParallelExecConstants.PROP_THREAD_NAME + "-" + i, execDesc, fileDesc, pluginEnv, osName, workingDirectory, execMavenPluginVersion, windowsExtension, getLog());
                executor.execute(worker);
                i++;
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        getLog().info("Finished all threads");
    }

}
