/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
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
package org.tawja.maven.plugins.frontend.resources;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

/**
 *
 * @author Adam Dubiel
 */
public class Resource {

    private final String resourceName;

    private final Set<Filter> filters = new HashSet<Filter>();

    private final Log logger;

    public static Resource from(String resourceName, Log logger) {
        return new Resource(resourceName, logger);
    }

    public Resource(String resourceName, Log logger) {
        this.resourceName = resourceName;
        this.logger = logger;
    }

    public Resource withFilter(String placeholder, String value) {
        this.filters.add(new Filter(placeholder, value));
        return this;
    }

    public void copy(String to) {
        copyResource(to, false);
    }

    public void copyAndOverwrite(String to) {
        copyResource(to, true);
    }

    private void copyResource(String targetPath, boolean overwrite) {
        try {
            String contents = filter(read());

            File targetFile = new File(targetPath);
            if (!targetFile.exists() || overwrite) {
                FileUtils.copyInputStreamToFile(contentAsInputStream(contents), targetFile);
            } else {
                logger.debug("Not overwriting file " + targetPath);
            }
        } catch (IOException exception) {
            throw new ResourceCreationException(resourceName, targetPath, exception);
        }
    }

    private InputStream contentAsInputStream(String content) {
        return new ByteArrayInputStream(content.getBytes());
    }

    private String read() throws IOException {
        InputStream stream = Resource.class.getResourceAsStream(resourceName);
        StringWriter contentsWriter = new StringWriter();

        IOUtils.copy(stream, contentsWriter);
        return contentsWriter.toString();
    }

    private String filter(String contents) {
        String filteredContents = contents;
        for (Filter filter : filters) {
            filteredContents = filter.filter(filteredContents);
        }
        return filteredContents;
    }
}
