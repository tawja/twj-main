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
package org.tawja.platform.modules.website.internal;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.ops4j.pax.web.extender.whiteboard.HttpContextMapping;
import org.ops4j.pax.web.extender.whiteboard.runtime.DefaultHttpContextMapping;
import org.ops4j.pax.web.extender.whiteboard.runtime.DefaultResourceMapping;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jbennani
 */
@Component(publicFactory = false)
@Provides
@Instantiate
public class TwjWebServerService {

    private static final Logger LOG = LoggerFactory.getLogger(TwjWebServerService.class);

    @Context
    private BundleContext context;

    @Requires
    HttpService httpService;

    @Validate
    public void start() {
        String currentDir = System.getProperty("user.dir");
        //try {
            DefaultHttpContextMapping httpContextMapping = new DefaultHttpContextMapping();
            //DefaultResourceMapping
            httpContextMapping.setHttpContextId("tawja-default");
            httpContextMapping.setPath("/");
            ////httpContextMapping.getHttpContext().
            ServiceRegistration<HttpContextMapping> httpContextMappingRegistration = context
                    .registerService(HttpContextMapping.class,
                            httpContextMapping, null);

            //httpService.registerResources("/", "/webapps/ROOT", httpContextMapping.getHttpContext());
            //httpService.registerResources("/en", "/webapps/en", httpContextMapping.getHttpContext());
            //httpService.registerResources("/fr", "/webapps/fr", httpContextMapping.getHttpContext());

            //httpService.registerResources("/", "/webapps/ROOT", null);
            //httpService.registerResources("/en", "/webapps/en", null);
            //httpService.registerResources("/fr", "/webapps/fr", null);
        //} catch (NamespaceException nsEx) {
        //    LOG.error("Unable to create web context resources", nsEx);
        //}
    }

    @Validate
    public void stop() {

    }
}
