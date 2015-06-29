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
package org.tawja.platform.plugins.database.base;

import org.apache.felix.ipojo.annotations.Property;
import org.tawja.platform.plugins.database.api.TwjDatabaseFactory;

/**
 *
 * @author jbennani
 */
public abstract class AbstractTwjDatabaseFactory<T> implements TwjDatabaseFactory<T> {

    @Property
    private String name;
    @Property
    private String type;

    public AbstractTwjDatabaseFactory() {
        super();
    }

    /**
     * @return the type
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * @param type the type to set
     */
    protected void setType(String type) {
        this.type = type;
    }

}
