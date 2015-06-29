/*
 * Copyright 2015 Jaafar BENNANI-SMIRES.
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
package org.tawja.maven.plugins.parallelexec.executable;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.tawja.maven.plugins.parallelexec.ParallelExecConstants;
import org.twdata.maven.mojoexecutor.MojoExecutor;

/**
 * Runnable that is reponible to send a signe command to exec-maven-plugin. Will
 * be used by *ExecMojo to build an executable queue that will be consumed by a
 * ThreadService in a Thread Pool.
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class WorkerThread implements Runnable {

    private final String name;
    private final Executable exec;
    private final MojoExecutor.ExecutionEnvironment pluginEnv;
    private final String execMavenPluginVersion;
    private final String windowsExtension;
    private final Log log;

    public WorkerThread(String name, ExecutableDescriptor execDesc, FileProcessDescriptor fileDesc, MojoExecutor.ExecutionEnvironment pluginEnv, String osName, String workingDirectory, String execMavenPluginVersion, String windowsExtension, Log log) {
        this.name = name;
        this.exec = new Executable(execDesc, fileDesc, osName, workingDirectory, windowsExtension);
        this.pluginEnv = pluginEnv;
        this.execMavenPluginVersion = execMavenPluginVersion;
        this.windowsExtension = windowsExtension;
        this.log = log;
    }

    @Override
    public void run() {
        log.debug(Thread.currentThread().getName() + " :  Start. Command = " + name);
        processCommand();
        log.debug(Thread.currentThread().getName() + " :  End.");
    }

    private void processCommand() {
        try {
            MojoExecutor.executeMojo(
                    MojoExecutor.plugin(
                            MojoExecutor.groupId(ParallelExecConstants.EXEC_MAVEN_GROUP),
                            MojoExecutor.artifactId(ParallelExecConstants.EXEC_MAVEN_ARTIFACT),
                            MojoExecutor.version(execMavenPluginVersion)),
                    MojoExecutor.goal(ParallelExecConstants.EXEC_MAVEN_GOAL),
                    MojoExecutor.configuration(exec.getConfiguration()),
                    pluginEnv
            );
        } catch (MojoExecutionException e) {
            log.error("WorkerThread : Exception while executing " + Thread.currentThread().getName() + " : " + name, e);
            //throw new MojoFailureException("WorkerThread : Exception while executing " + Thread.currentThread().getName() + " : " + name, e);

        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
