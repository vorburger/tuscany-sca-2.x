/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.tuscany.spi.model.physical;

import java.net.URI;

import org.apache.tuscany.spi.model.ModelObject;

/**
 * Represents a physical component model.
 *
 * @version $Rev$ $Date$
 */
public abstract class PhysicalComponentDefinition extends ModelObject {

    // Component Id.
    private URI componentId;
    
    // Instance factory provider
    private InstanceFactoryProviderDefinition instanceFactoryProviderDefinition;

    /**
     * Gets the component id.
     *
     * @return Component id.
     */
    public URI getComponentId() {
        return componentId;
    }

    /**
     * Sets the component id.
     *
     * @param componentId
     */
    public void setComponentId(URI componentId) {
        this.componentId = componentId;
    }

    /**
     * Gets the instance factory provider definition.
     * @return Instance factory provider definition.
     */
    public InstanceFactoryProviderDefinition getInstanceFactoryProviderDefinition() {
        return instanceFactoryProviderDefinition;
    }

    /**
     * Sets the instance factory provider definition.
     * @param instanceFactoryProviderDefinition Instance factory provider definition.
     */
    public void setInstanceFactoryProviderDefinition(InstanceFactoryProviderDefinition instanceFactoryProviderDefinition) {
        this.instanceFactoryProviderDefinition = instanceFactoryProviderDefinition;
    }

}
