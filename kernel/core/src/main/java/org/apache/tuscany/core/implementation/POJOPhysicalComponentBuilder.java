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
package org.apache.tuscany.core.implementation;

import org.osoa.sca.annotations.Init;
import org.osoa.sca.annotations.Reference;

import org.apache.tuscany.core.component.instancefactory.IFProviderBuilderRegistry;
import org.apache.tuscany.core.implementation.system.model.SystemPhysicalComponentDefinition;
import org.apache.tuscany.spi.builder.physical.PhysicalComponentBuilder;
import org.apache.tuscany.spi.builder.physical.PhysicalComponentBuilderRegistry;
import org.apache.tuscany.spi.builder.physical.WireAttacherRegistry;
import org.apache.tuscany.spi.component.Component;
import org.apache.tuscany.spi.component.ScopeRegistry;
import org.apache.tuscany.spi.model.physical.PhysicalComponentDefinition;
import org.apache.tuscany.spi.services.classloading.ClassLoaderRegistry;

/**
 * Base class for PhysicalComponentBuilders that build components based on POJOs.
 *
 * @version $Rev$ $Date$
 */
public abstract class POJOPhysicalComponentBuilder<PCD extends PhysicalComponentDefinition, C extends Component>
    implements PhysicalComponentBuilder<PCD, C> {

    protected final PhysicalComponentBuilderRegistry builderRegistry;
    protected final WireAttacherRegistry wireAttacherRegistry;
    protected final ScopeRegistry scopeRegistry;
    protected final IFProviderBuilderRegistry providerBuilders;
    protected final ClassLoaderRegistry classLoaderRegistry;

    protected POJOPhysicalComponentBuilder(
        @Reference(name = "builderRegistry")PhysicalComponentBuilderRegistry builderRegistry,
        @Reference(name = "wireAttacherRegistry")WireAttacherRegistry wireAttacherRegistry,
        @Reference(name = "scopeRegistry")ScopeRegistry scopeRegistry,
        @Reference(name = "providerBuilders")IFProviderBuilderRegistry providerBuilders,
        @Reference(name = "classloaderRegistry")ClassLoaderRegistry classLoaderRegistry) {
        this.builderRegistry = builderRegistry;
        this.wireAttacherRegistry = wireAttacherRegistry;
        this.scopeRegistry = scopeRegistry;
        this.providerBuilders = providerBuilders;
        this.classLoaderRegistry = classLoaderRegistry;
    }
}
