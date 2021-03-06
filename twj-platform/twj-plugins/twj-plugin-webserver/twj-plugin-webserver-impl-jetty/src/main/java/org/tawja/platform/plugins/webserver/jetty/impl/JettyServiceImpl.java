/**
 * Copyright (C) 2015 Tawja (http://www.tawja.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tawja.platform.plugins.webserver.jetty.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;

/**
 * <code>LogServiceImpl</code> is a simple OSGi LogService implementation that delegates to a slf4j
 * Logger.
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class JettyServiceImpl {// implements LogService {

	private static final String UNKNOWN = "[Unknown]";

	//private final Logger delegate;


	/**
	 * Creates a new instance of LogServiceImpl.
	 *
	 * @param bundle The bundle to create a new LogService for.
	 */
	public JettyServiceImpl(Bundle bundle) {

		String name = bundle.getSymbolicName();
		Version version = bundle.getVersion();
		if (version == null) {
			version = Version.emptyVersion;
		}
		//delegate = LoggerFactory.getLogger(name + '.' + version);
	}

}
