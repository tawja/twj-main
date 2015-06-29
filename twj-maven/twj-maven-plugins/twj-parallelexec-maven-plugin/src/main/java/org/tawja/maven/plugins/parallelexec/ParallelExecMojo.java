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

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.tawja.maven.plugins.parallelexec.executable.ExecutableDescriptor;

import java.util.Arrays;
import java.util.List;

/**
 * Executes npm install to download all dependencies declared in package.json.
 *
 * @author Adam DUBIEL
 * @author Jaafar BENNANI-SMIRES
 */
@Mojo(name = "exec", defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ParallelExecMojo extends AbstractExecutableMojo {

    @Override
    protected void initializeExecutableDescriptors() {

        executableDescriptors.clear();
        
        ExecutableDescriptor executableDesc = new ExecutableDescriptor(executable);
        executableDesc.addEnvironmentVars(environmentVar);
        //executableDesc.addNormalizedArguments(options, "=");
        executableDesc.addArguments(arguments);

        // TODO : Manage multiple executables
        executableDescriptors.add(executableDesc);
    }

}
