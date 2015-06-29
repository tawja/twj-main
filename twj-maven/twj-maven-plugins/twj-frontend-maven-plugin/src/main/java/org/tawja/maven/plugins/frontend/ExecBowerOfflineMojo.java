/*
 * Copyright 2014 original author or authors.
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

import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.tawja.maven.plugins.frontend.archive.TarUtil;
import org.tawja.maven.plugins.frontend.executable.Executable;

/**
 * Unpack bower_components.tar and, Run BOWER install.
 *
 * @author Adam Dubiel
 */
@Mojo(name = "bower-offline", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true)
public class ExecBowerOfflineMojo extends ExecBowerMojo {

    private static final String BOWER_COMPONENTS_DIR_NAME = "bower_components";

    @Override
    protected List<Executable> getExecutables() {
        unpackComponents();
        return Arrays.asList(createBowerInstallExecutable());
    }

    private void unpackComponents() {
        String bowerComponentsPath = gruntBuildDirectory + File.separator + BOWER_COMPONENTS_DIR_NAME;
        File targetComponentsPath = new File(bowerComponentsPath);
        if (targetComponentsPath.exists()) {
            getLog().info("Found existing bower_components at " + bowerComponentsPath + " , not going to overwrite them.");
            return;
        }

        if (bowerOfflineComponentsFilePath == null) {
            bowerOfflineComponentsFilePath = relativeFrontendSourceDirectory();
        }

        File offlineModules = new File(basedir() + File.separator + bowerOfflineComponentsFilePath + File.separator + bowerOfflineComponentsFile);
        File targetPath = new File(gruntBuildDirectory);

        TarUtil.untar(offlineModules, targetPath, getLog());
    }
}
