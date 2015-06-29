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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.tawja.maven.plugins.parallelexec.ParallelExecConstants;
import org.tawja.maven.plugins.parallelexec.resources.Filter;

/**
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class FileProcessDescriptor {
    private final File inputFile;
    private File outputFile;
    private final List<Filter> argumentFilters = new ArrayList<>();

    public FileProcessDescriptor(File file, String sourceDirectory, String targetDirectory, String targetExtension)
    {
        this.inputFile = file;
        defineFiltersFromInput(sourceDirectory, targetDirectory, targetExtension);
    }
    
    private void defineFiltersFromInput(String sourceDirectory, String targetDirectory, String targetExtension)
    {
        // Define argument filtes that will be used to manage finale executables from descriptors
        argumentFilters.clear();
        // IN
        String inputParentDir = FilenameUtils.getFullPathNoEndSeparator(inputFile.getAbsolutePath());
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".in.fullpath", inputParentDir));
        String filePathRelativeToSource = FilenameUtils.separatorsToSystem(FilenameUtils.getFullPathNoEndSeparator(new File(sourceDirectory).toURI().relativize(inputFile.getParentFile().toURI()).getPath()));
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".in.path", filePathRelativeToSource));
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".in.fullname", inputFile.getName()));
        String fileNameWithOutExt = FilenameUtils.removeExtension(inputFile.getName());
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".in.name", fileNameWithOutExt));
        String fileInExt = FilenameUtils.getExtension(inputFile.getName());
        if(targetExtension == null || targetExtension.isEmpty())
        {
            targetExtension = fileInExt;
        }
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".in.extension", fileInExt));
        // OUT
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".out.path", filePathRelativeToSource));

        String outputParentDir = new File(targetDirectory).getAbsolutePath();
        if (filePathRelativeToSource != null && !filePathRelativeToSource.isEmpty()) {
            outputParentDir = outputParentDir + File.separator + filePathRelativeToSource;
        }
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".out.fullpath", outputParentDir));
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".out.extension", targetExtension));
        
        // Final input-output names
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".inputFilePath", inputParentDir + File.separator + fileNameWithOutExt + "." + fileInExt  ));
        String outputFilePath = outputParentDir + File.separator + fileNameWithOutExt + "." + targetExtension;
        argumentFilters.add(new Filter(ParallelExecConstants.PROP_PREFIX + ".outputFilePath", outputFilePath));
        outputFile = new File(outputFilePath);
        
        // TO Remove from here !!
        File outParDirFile = new File(outputParentDir);
        if(!outParDirFile.exists())
        {
            outParDirFile.mkdirs();
        }
    }

    /**
     * @return the argumentFilters
     */
    public List<Filter> getArgumentFilters() {
        return argumentFilters;
    }
    
    /**
     * @return the inputFile
     */
    public File getInputFile() {
        return inputFile;
    }

    /**
     * @return the outputile
     */
    public File getOutputFile() {
        return outputFile;
    }

}
