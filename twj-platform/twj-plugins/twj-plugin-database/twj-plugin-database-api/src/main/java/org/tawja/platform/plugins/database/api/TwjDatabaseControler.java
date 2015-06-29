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
package org.tawja.platform.plugins.database.api;

import java.util.Map;

/**
 *
 * @author jbennani
 */
public interface TwjDatabaseControler {

    //public void addFactory(TwjDatabaseFactory dbFactory);

    //public void removeFactory(TwjDatabaseFactory dbFactory);
    
    public TwjDatabaseContainer getContainer();

    public Map<String, TwjDatabaseFactory> getFactories();
    
    ///public TwjDatabaseServer createtServer(String serverType);
    ///public TwjDatabaseServer createtServer(String serverType, String name, String host, int port);

    ///public TwjDatabase createtDatabase(String serverType);
    /// TwjDatabase createtDa(String serverType, String name, String host, int port);
}
