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
package org.tawja.platform.plugins.utils.commands.configuration;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Properties;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;


/**
 * ConfigurationAdmin commands giving utilities for configuration
 *
 * @author jbennani
 */
@Component(publicFactory = false, immediate = true)
@Instantiate
@Provides
public class TwjConfigAdminCommands {

    /**
     * Defines the command scope (ipojo).
     */
    @ServiceProperty(name = "osgi.command.scope", value = "tawja")
    String m_scope;

    /**
     * Defines the functions (commands).
     */
    @ServiceProperty(name = "osgi.command.function", value = "{}")
    String[] m_function = new String[]{
        "configList",
        "configCreate",
        "configUpdate",
        "configDelete"
    };

    /**
     * Configuration Admin service dependency.
     */
    @Requires
    ConfigurationAdmin pm;

    /**
     * Command list configurations from the config admin
     */
    @Descriptor("Command list configurations from the config admin")
    public void configList() {
        Configuration[] confs = null;
        try {
            confs = pm.listConfigurations(null);
            for (int i = 0; i < confs.length; i++) {
                System.out.println(confs[i].getPid() + " : " + confs[i].getProperties());
            }
        } catch (IOException e) {
            System.err.println("An error occurs when listing configurations : " + e.getMessage());
        } catch (InvalidSyntaxException e) {
            System.err.println("An error occurs when listing configurations : " + e.getMessage());
        }
    }

    /**
     * Command to insert a new factory configuration in the config admin.
     * Usage : [component-type] [property-name=property-value]*
     */
    @Descriptor("Command to insert a new factory configuration in the config admin")
    public void configCreate(String factoryPid, String pid, String[] properties) {
        Properties props = new Properties();
        props.put("service.factoryPid", factoryPid);
        //props.put("service.pid", pid);

        for (int i = 0; i < properties.length; i++) {
            String[] prop = properties[i].split("=");
            if (prop.length != 2) {
                System.err.println("Wrong property definition : name=value");
                return;
            }
            props.put(prop[0], prop[1]);
        }

        System.out.println("Insert the configuration : " + props);

        try {
            Configuration conf = pm.createFactoryConfiguration(factoryPid, "?");
            conf.update(props);
            System.out.println("Created configuration: " + conf.getPid());
        } catch (IOException e) {
            System.err.println("An error occurs when inserting a configuration : " + e.getMessage());
        }
    }

    /**
     * Command to update an existing configuration in the config admin
     * Usage : [instance-name] [property-name=property-value]*
     */
    @Descriptor("Command to update an existing configuration in the config admin")
    public void configUpdate(String instanceName, String[] properties) {
        try {

            Configuration conf = pm.getConfiguration(instanceName);
            Dictionary props = new Properties();

            for (int i = 0; i < properties.length; i++) {
                String[] prop = properties[i].split("=");
                if (prop.length != 2) {
                System.err.println("Wrong property definition : name=value");
                    return;
                }
                props.put(prop[0], prop[1]);
            }

            conf.update(props);

            System.out.println("Update the configuration : " + props);

        } catch (IOException e) {
            System.err.println("An error occurs when updating a configuration : " + e.getMessage());
        }
    }

    /**
     * Command to delete a factory configuration from the config admin
     * Usage : [instance-name]
     * If instance-name=all, delete all configuration0
     */
    @Descriptor("Command to delete a factory configuration from the config admin")
    public void configDelete(String intancename) {
        if (intancename.equals("all")) {
            deleteAll();
            return;
        }

        System.out.println("Delete the configuration : " + intancename);

        try {
            Configuration conf = pm.getConfiguration(intancename);
            conf.delete();
        } catch (IOException e) {
            System.err.println("An error occurs when deleting a configuration : " + e.getMessage());
        }
    }
    /**
     * Delete all configurations
     */
    private void deleteAll() {
        Configuration[] confs;
        try {
            confs = pm.listConfigurations(null);
            for (int i = 0; i < confs.length; i++) {
                System.out.println("Delete the configuration : " + confs[i].getProperties());
                confs[i].delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
    }

}
