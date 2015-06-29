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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import org.tawja.maven.plugins.frontend.executable.Executable;

/**
 * Executes bower install to download all dependencies declared in bower.json.
 */
@Mojo(name = "bower", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true)
public class ExecBowerMojo extends AbstractExecutableMojo {

    protected static final String BOWER_INSTALL_COMMAND = "install";

    /**
     * Name of bower executable in PATH, defaults to 'bower' (global PATH).
     */
    @Parameter(property = "bowerExecutable", defaultValue = "bower")
    protected String bowerExecutable;

    /**
     * List of options passed to bower.
     */
    @Parameter(property = "bowerOptions")
    private String[] bowerOptions;

    /**
     * Map of environment variables passed to bower install.
     */
    @Parameter
    protected Map<String, String> bowerEnvironmentVar;
    
    @Override
    protected List<Executable> getExecutables() {
        Executable executable = new Executable(bowerExecutable);
        executable.addArgument(BOWER_INSTALL_COMMAND);
        appendNoColorsArgument(executable);
        appendAllowRootArgument(executable);
        appendBowerOptions(executable);
        return Arrays.asList(executable);
    }


    protected Executable createBowerInstallExecutable() {
        Executable executable = new Executable(bowerExecutable);
        executable.addArgument(BOWER_INSTALL_COMMAND);
        appendNoColorsArgument(executable);
        appendAllowRootArgument(executable);
        appendBowerOptions(executable);
        executable.addEnvironmentVars(bowerEnvironmentVar);
        return executable;
    }

    protected void appendBowerOptions(Executable executable) {
        executable.addNormalizedArguments(bowerOptions, "=");
    }

    protected void appendNoColorsArgument(Executable executable) {
        if (!showColors) {
            executable.addArgument("--color=false");
        }
    }
    
    // Change bower default behaviour by allowing root by default... for the moment easier for factory build... need to be changed!!!
    protected void appendAllowRootArgument(Executable executable) {
        if (allowRoot) {
            executable.addArgument("--allow-root");
        }
    }
}
