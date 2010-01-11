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
package org.apache.tuscany.sca.binding.ws.axis2;

import org.apache.tuscany.sca.binding.ws.WebServiceBinding;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.core.FactoryExtensionPoint;
import org.apache.tuscany.sca.databinding.DataBindingExtensionPoint;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.host.http.ServletHostHelper;
import org.apache.tuscany.sca.provider.BindingProviderFactory;
import org.apache.tuscany.sca.provider.ReferenceBindingProvider;
import org.apache.tuscany.sca.provider.ServiceBindingProvider;
import org.apache.tuscany.sca.runtime.RuntimeEndpoint;
import org.apache.tuscany.sca.runtime.RuntimeEndpointReference;

/**
 * Axis2BindingProviderFactory
 *
 * @version $Rev$ $Date$
 */

public class Axis2BindingProviderFactory implements BindingProviderFactory<WebServiceBinding> {

    private FactoryExtensionPoint modelFactories;
    private ServletHost servletHost;
    private DataBindingExtensionPoint dataBindings;

    public Axis2BindingProviderFactory(ExtensionPointRegistry extensionPoints) {
        this.servletHost = ServletHostHelper.getServletHost(extensionPoints);
        modelFactories = extensionPoints.getExtensionPoint(FactoryExtensionPoint.class);
        dataBindings = extensionPoints.getExtensionPoint(DataBindingExtensionPoint.class);
    }

    public ReferenceBindingProvider createReferenceBindingProvider(RuntimeEndpointReference endpointReference) {
        return new Axis2ReferenceBindingProvider(endpointReference, modelFactories, dataBindings);
    }

    public ServiceBindingProvider createServiceBindingProvider(RuntimeEndpoint endpoint) {
        return new Axis2ServiceBindingProvider(endpoint, servletHost, modelFactories, dataBindings);
    }
    
    public Class<WebServiceBinding> getModelType() {
        return WebServiceBinding.class;
    }
}
