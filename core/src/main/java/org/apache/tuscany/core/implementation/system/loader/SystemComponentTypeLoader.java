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
package org.apache.tuscany.core.implementation.system.loader;

import java.net.URL;

import org.apache.tuscany.core.implementation.IntrospectionRegistry;
import org.apache.tuscany.core.implementation.Introspector;
import org.apache.tuscany.core.implementation.PojoComponentType;
import org.apache.tuscany.core.implementation.ProcessingException;
import org.apache.tuscany.core.implementation.system.model.SystemImplementation;
import org.apache.tuscany.core.util.JavaIntrospectionHelper;
import org.apache.tuscany.spi.annotation.Autowire;
import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.extension.ComponentTypeLoaderExtension;
import org.apache.tuscany.spi.loader.LoaderException;
import org.apache.tuscany.spi.loader.LoaderRegistry;

/**
 * Loads a system component type
 *
 * @version $Rev$ $Date$
 */
public class SystemComponentTypeLoader extends ComponentTypeLoaderExtension<SystemImplementation> {
    private Introspector introspector;

    public SystemComponentTypeLoader() {
    }

    public SystemComponentTypeLoader(Introspector introspector) {
        this.introspector = introspector;
    }

    public SystemComponentTypeLoader(LoaderRegistry loaderRegistry, Introspector introspector) {
        super(loaderRegistry);
        this.introspector = introspector;
    }

    //FIXME autowire to support multiple interfaces
    @Autowire
    public void setIntrospector(IntrospectionRegistry introspector) {
        this.introspector = introspector;
    }

    public void load(CompositeComponent<?> parent, SystemImplementation implementation,
                     DeploymentContext deploymentContext) throws LoaderException {
        Class<?> implClass = implementation.getImplementationClass();
        URL sidefile = implClass.getResource(JavaIntrospectionHelper.getBaseName(implClass) + ".componentType");
        PojoComponentType componentType;
        if (sidefile == null) {
            componentType = loadByIntrospection(parent, implementation, deploymentContext);
        } else {
            componentType = loadFromSidefile(sidefile, deploymentContext);
        }
        implementation.setComponentType(componentType);
    }

    protected Class<SystemImplementation> getImplementationClass() {
        return SystemImplementation.class;
    }

    protected PojoComponentType loadByIntrospection(CompositeComponent<?> parent,
                                                    SystemImplementation implementation,
                                                    DeploymentContext deploymentContext) throws ProcessingException {
        PojoComponentType componentType = new PojoComponentType();
        Class<?> implClass = implementation.getImplementationClass();
        introspector.introspect(parent, implClass, componentType, deploymentContext);
        return componentType;
    }


    protected PojoComponentType loadFromSidefile(URL url, DeploymentContext deploymentContext) throws LoaderException {
        return loaderRegistry.load(null, url, PojoComponentType.class, deploymentContext);
    }
}
