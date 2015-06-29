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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.tawja.maven.plugins.parallelexec.ParallelExecConstants;
import org.tawja.maven.plugins.parallelexec.resources.Filter;
import org.twdata.maven.mojoexecutor.MojoExecutor;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;

/**
 * Implementation of an ExecutableDescriptor that reprensent a simple execution
 * on a FileDescriptor and a Confifuration
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class Executable {

    private final ExecutableDescriptor execDesc;
    private final FileProcessDescriptor fileProcessDesc;
    private MojoExecutor.Element[] configuration;

    private final String osName;
    private final String workingDirectory;
    private final String windowsExtension;
    
    private MojoExecutor.Element[] argumentsArray;

    public Executable(ExecutableDescriptor execDesc, FileProcessDescriptor fileProcessDesc, String osName, String workingDirectory, String windowsExtension) {
        this.execDesc = execDesc;
        this.fileProcessDesc = fileProcessDesc;
        this.osName = osName;
        this.workingDirectory = workingDirectory;
        this.windowsExtension = windowsExtension;

        buildFromDescriptors();
    }

    private void buildFromDescriptors() {
        buildArguments();
        buildConfigurationForOS();
        updateConfigurationForCustomSuccessCodes();

    }

    public MojoExecutor.Element[] getArgumentsArray() {
        return argumentsArray;
    }

    /**
     * Create Arguments Array ready for the executable configuration
     */
    private void buildArguments() {
        argumentsArray = new MojoExecutor.Element[execDesc.getArguments().size()];

        int i = 0;
        for (String arg : execDesc.getArguments()) {
            for (Filter filter : fileProcessDesc.getArgumentFilters()) {
                arg = filter.filter(arg);
            }
            argumentsArray[i] = MojoExecutor.element(MojoExecutor.name(ParallelExecConstants.EXEC_ARGUMENT_NAME), arg);
            i++;
        }
    }

    /**
     * @return the configuration
     */
    public MojoExecutor.Element[] getConfiguration() {
        return configuration;
    }

    /**
     * Update
     * <pre>configuration</pre> element for success codes.
     */
    private void updateConfigurationForCustomSuccessCodes() {
        if (execDesc.overrideSuccessCodes()) {
            MojoExecutor.Element[] successCodesElements = new MojoExecutor.Element[execDesc.successCodes().length];
            for (int index = 0; index < execDesc.successCodes().length; ++index) {
                successCodesElements[index] = element(ParallelExecConstants.EXEC_SUCCESS_CODE_ELEMENT, execDesc.successCodes()[index]);
            }
            MojoExecutor.Element customSuccessCodes = element(ParallelExecConstants.EXEC_SUCCESS_CODES_ELEMENT, successCodesElements);
            configuration = concat(configuration, customSuccessCodes);
        }
    }

    /**
     * Create
     * <pre>configuration</pre> element for host OS.
     */
    private void buildConfigurationForOS() {
        //MojoExecutor.Element[] configuration;

        if (osName.toUpperCase().contains(ParallelExecConstants.WINDOWS_OS_FAMILY.toUpperCase())) {
            configuration = buildConfigForWindows();
        } else {
            configuration = buildConfigForProperOS();
        }

        configuration = concat(configuration, new MojoExecutor.Element[]{element(name("workingDirectory"), workingDirectory)});

        if (execDesc.hasEnvironmentVars()) {
            configuration = concat(configuration, element("environmentVariables", elementsFromMap(execDesc.getEnvironmentVars())));
        }
    }

    /**
     * Create
     * <pre>configuration</pre> element for proper *nix OSes.
     *
     * @return configuration
     */
    private MojoExecutor.Element[] buildConfigForProperOS() {
        MojoExecutor.Element[] osConfiguration = new MojoExecutor.Element[]{
            element(name("executable"), execDesc.executableName()),
            element(name("arguments"), argumentsArray)
        };

        return osConfiguration;
    }

    /**
     * Create
     * <pre>configuration</pre> element for strange Windows OS.
     *
     * @return configuration
     */
    private MojoExecutor.Element[] buildConfigForWindows() {
        MojoExecutor.Element[] args;

        if (execDesc.executableName() != null) {
            String execName = execDesc.executableName();
            if (windowsExtension != null && !windowsExtension.isEmpty()){
                execName = FilenameUtils.removeExtension(execName) + "." + windowsExtension;
            }
            
            args = new MojoExecutor.Element[]{
                element(name("argument"), "/C"),
                element(name("argument"), execName)
            };
        } else {
            args = new MojoExecutor.Element[]{
                element(name("argument"), "/C")};
        }

        args = concat(args, argumentsArray);

        MojoExecutor.Element[] osConfiguration = new MojoExecutor.Element[]{
            element(name("executable"), "cmd"),
            element(name("arguments"), args)
        };

        return osConfiguration;
    }

    /**
     * Don't want to use any dependencies like Apache Commons - sorry.
     *
     * @param <T> type of array entries
     * @param array1 first array
     * @param array2 second array
     * @return first array + second array
     */
    protected <T> T[] concat(T[] array1, T... array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    private MojoExecutor.Element[] elementsFromMap(Map<String, String> elements) {
        List<MojoExecutor.Element> elementList = new ArrayList<>();
        for (Map.Entry<String, String> entry : elements.entrySet()) {
            elementList.add(element(entry.getKey(), entry.getValue()));
        }

        return elementList.toArray(new MojoExecutor.Element[elementList.size()]);
    }

    /**
     * @return the execDesc
     */
    public ExecutableDescriptor getExecutableDescriptor() {
        return execDesc;
    }

    /**
     * @return the fileProcessDesc
     */
    public FileProcessDescriptor getFileProcessDescriptor() {
        return fileProcessDesc;
    }

    /**
     * @return the fileProcessDesc
     */
    public FileProcessDescriptor getFileProcessDesc() {
        return fileProcessDesc;
    }

}
