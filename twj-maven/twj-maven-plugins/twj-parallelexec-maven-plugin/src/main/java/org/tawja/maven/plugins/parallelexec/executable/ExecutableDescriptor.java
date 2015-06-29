/*
 * Copyright 2014 Adam Dubiel.
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.twdata.maven.mojoexecutor.MojoExecutor;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;

/**
 * Represent the information sent to exec-maven-plugin to execute a command
 * line.
 *
 * @author Adam DUBIEL
 * @author Jaafar BENNANI-SMIRES
 */
public class ExecutableDescriptor {

    /**
     * Pattern for detecting options with whitespace characters. Anything after
     * first whitespace is ignored by exec-maven-plugin, so they need to be
     * transformed, ex: --option true == --option=true
     */
    private static final Pattern WHITESPACED_OPTION_PATTERN = Pattern.compile("^-{1,2}?[\\w-]*\\s+");

    private final List<String> arguments = new ArrayList<>();
    private final Map<String, String> environmentVars = new HashMap<>();
    private final String executableName;
    private final String[] successCodes;

    public ExecutableDescriptor(String executableName, String[] successCodes) {
        this.executableName = executableName;
        this.successCodes = successCodes;
    }

    public ExecutableDescriptor(String executableName) {
        this(executableName, null);
    }

    ExecutableDescriptor(ExecutableDescriptor executable) {
        this.executableName = executable.executableName();
        this.arguments.clear();
        this.arguments.addAll(executable.getArguments());
        this.successCodes = executable.successCodes();
    }

    public void addArgument(String value) {
        //arguments.add(element(name(ARGUMENT_NAME), value));
        arguments.add(value);
    }

    public void addArguments(String[] values) {
        if (values != null) {
            for (String value : values) {
                addArgument(value);
            }
        }
    }

    public void addEnvironmentVars(Map<String, String> environmentVars) {
        if (environmentVars != null) {
            this.environmentVars.putAll(environmentVars);
        }
    }

    /**
     * Normalization checks if argument contains whitespace character between
     * argument name and it's value, if so it replaces the whitespace with
     * provided replacement. Normalization is needed, because mojo-exec discards
     * all characters after first whitespace.
     *
     * @param value
     * @param whitespaceReplacement
     */
    /*
     public void addNormalizedArgument(String value, String whitespaceReplacement) {
     addArgument(normalizeArgument(value, whitespaceReplacement));
     }

     public void addNormalizedArguments(String[] values, String whitespaceReplacement) {
     if (values != null) {
     for (String value : values) {
     addNormalizedArgument(value, whitespaceReplacement);
     }
     }
     }
     */
    /**
     *
     * @return arguments
     */
    public List<String> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    /*
     public MojoExecutor.Element[] argumentsArray() {
     return arguments.toArray(new MojoExecutor.Element[arguments.size()]);
     }

     public void clearArguments() {
     arguments.clear();
     }
     */
    public Map<String, String> getEnvironmentVars() {
        return Collections.unmodifiableMap(environmentVars);
    }

    public String executableName() {
        return executableName;
    }

    public boolean hasEnvironmentVars() {
        return !environmentVars.isEmpty();
    }

    /*
     private String normalizeArgument(String argument, String whitespaceReplacement) {
     Matcher matcher = WHITESPACED_OPTION_PATTERN.matcher(argument);
     if (matcher.find()) {
     return argument.replaceFirst("\\s+", whitespaceReplacement);
     }
     return argument;
     }
     */
    public boolean overrideSuccessCodes() {
        return successCodes != null;
    }

    public String[] successCodes() {
        return successCodes;
    }
}
