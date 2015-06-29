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
package org.tawja.plugins.slf4j.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>LogServiceImpl</code> is a simple OSGi LogService implementation that delegates to a slf4j
 * Logger.
 *
 * @author Jaafar BENNANI-SMIRES
 */
public class LogServiceImpl implements LogService {

	private static final String UNKNOWN = "[Unknown]";

	private final Logger delegate;


	/**
	 * Creates a new instance of LogServiceImpl.
	 *
	 * @param bundle The bundle to create a new LogService for.
	 */
	public LogServiceImpl(Bundle bundle) {

		String name = bundle.getSymbolicName();
		Version version = bundle.getVersion();
		if (version == null) {
			version = Version.emptyVersion;
		}
		delegate = LoggerFactory.getLogger(name + '.' + version);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.service.log.LogService#log(int, java.lang.String)
	 */
        @Override
	public void log(int level, String message) {

		switch (level) {
		case LOG_DEBUG:
			delegate.debug(message);
			break;
		case LOG_ERROR:
			delegate.error(message);
			break;
		case LOG_INFO:
			delegate.info(message);
			break;
		case LOG_WARNING:
			delegate.warn(message);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.service.log.LogService#log(int, java.lang.String,
	 *      java.lang.Throwable)
	 */
        @Override
	public void log(int level, String message, Throwable exception) {
	
	switch (level) {
		case LOG_DEBUG:
			delegate.debug(message, exception);
			break;
		case LOG_ERROR:
			delegate.error(message, exception);
			break;
		case LOG_INFO:
			delegate.info(message, exception);
			break;
		case LOG_WARNING:
			delegate.warn(message, exception);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.service.log.LogService#log(org.osgi.framework.ServiceReference,
	 *      int, java.lang.String)
	 */
        @Override
	public void log(ServiceReference sr, int level, String message) {

		switch (level) {
		case LOG_DEBUG:
			if(delegate.isDebugEnabled()){
				delegate.debug(createMessage(sr, message));
			}
			break;
		case LOG_ERROR:
			if(delegate.isErrorEnabled()){
				delegate.error(createMessage(sr, message));
			}
			break;
		case LOG_INFO:
			if(delegate.isInfoEnabled()){
				delegate.info(createMessage(sr, message));
			}
			break;
		case LOG_WARNING:
			if(delegate.isWarnEnabled()){
				delegate.warn(createMessage(sr, message));
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Formats the log message to indicate the service sending it, if known.
	 *
	 * @param sr the ServiceReference sending the message.
	 * @param message The message to log.
	 * @return The formatted log message.
	 */
	private String createMessage(ServiceReference sr, String message) {

		StringBuilder output = new StringBuilder();
		if (sr != null) {
			output.append('[').append(sr.toString()).append(']');
		} else {
			output.append(UNKNOWN);
		}
		output.append(message);

		return output.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.osgi.service.log.LogService#log(org.osgi.framework.ServiceReference,
	 *      int, java.lang.String, java.lang.Throwable)
	 */
        @Override
	public void log(ServiceReference sr, int level, String message, Throwable exception) {

		switch (level) {
		case LOG_DEBUG:
			if(delegate.isDebugEnabled()){
				delegate.debug(createMessage(sr, message), exception);
			}
			break;
		case LOG_ERROR:
			if(delegate.isErrorEnabled()){
				delegate.error(createMessage(sr, message), exception);
			}
			break;
		case LOG_INFO:
			if(delegate.isInfoEnabled()){
				delegate.info(createMessage(sr, message), exception);
			}
			break;
		case LOG_WARNING:
			if(delegate.isWarnEnabled()){
				delegate.warn(createMessage(sr, message), exception);
			}
			break;
		default:
			break;
		}
	}
}
