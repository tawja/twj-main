/**
 * Copyright (C) 2015 Tawja (http://www.tawja.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tawja.platform.modules.website.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.tawja.platform.commons.osgi.TwjAbstractActivator;

/**
 * <code>Activator</code> implements a simple bundle that registers a
 * {@link LogServiceFactory} for the creation of {@link LogService}
 * implementations.
 *
 * @author Jaafar BENNANI-SMIRES
 *
 */
public class Activator extends TwjAbstractActivator {
    
    

    /**
     *
     * Implements <code>BundleActivator.start()</code> to register a
     * LogServiceFactory.
     *
     * @param bundleContext the framework context for the bundle
     * @throws Exception
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        // Without Whiteboard
        String currentDir = System.getProperty("user.dir");
         ServiceReference sRef = bundleContext.getServiceReference(HttpService.class.getName());
         if (sRef != null) {
         HttpService service = (HttpService) bundleContext.getService(sRef);
         service.registerResources("/", "/webapps/ROOT", null);
         service.registerResources("/en","/webapps/en", null);
         service.registerResources("/fr", "/webapps/fr", null);
         }

        // With Whiteboard
        /*
        DefaultHttpContext myContext = new DefaultHttpContext();
         ServiceReference sRef = bundleContext.getServiceReference(HttpService.class.getName());
         if (sRef != null) {
         HttpService service = (HttpService) bundleContext.getService(sRef);
         service.registerResources("/", "/webappstest/ROOT", myContext);
         service.registerResources("/en", "/webappstest/en", myContext);
         service.registerResources("/fr", "/webappstest/fr", myContext);
         }
        */
    }

    /**
     *
     * Implements <code>BundleActivator.stop()</code>.
     *
     * @param bundleContext the framework context for the bundle
     * @throws Exception
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {

        // Note: It is not required that we remove the service here, since
        // the framework will do it automatically. 
    }
}
