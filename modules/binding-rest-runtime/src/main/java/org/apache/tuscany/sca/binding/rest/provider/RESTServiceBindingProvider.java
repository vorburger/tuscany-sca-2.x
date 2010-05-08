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

package org.apache.tuscany.sca.binding.rest.provider;

import javax.servlet.Servlet;

import org.apache.tuscany.sca.binding.rest.RESTBinding;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.host.http.ServletHost;
import org.apache.tuscany.sca.interfacedef.InterfaceContract;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.InvocationChain;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.invocation.Phase;
import org.apache.tuscany.sca.provider.EndpointProvider;
import org.apache.tuscany.sca.provider.OperationSelectorProvider;
import org.apache.tuscany.sca.provider.OperationSelectorProviderFactory;
import org.apache.tuscany.sca.provider.ProviderFactoryExtensionPoint;
import org.apache.tuscany.sca.provider.WireFormatProvider;
import org.apache.tuscany.sca.provider.WireFormatProviderFactory;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;
import org.apache.tuscany.sca.runtime.RuntimeEndpoint;

/**
 * Implementation of an HTTP binding provider.
 *
 * @version $Rev$ $Date$
 */
public class RESTServiceBindingProvider implements EndpointProvider {
    private ExtensionPointRegistry extensionPoints;
    
    private RuntimeEndpoint endpoint;
    private RuntimeComponent component;
    private RuntimeComponentService service;  
    private InterfaceContract serviceContract;
    private RESTBinding binding;
    private MessageFactory messageFactory;
    
    private OperationSelectorProvider osProvider;
    private WireFormatProvider wfProvider;
    
    private ServletHost servletHost;
    private String servletMapping;
    private RESTBindingListenerServlet bindingListenerServlet;
   
    public RESTServiceBindingProvider(RuntimeEndpoint endpoint,
                                      ExtensionPointRegistry extensionPoints,
                                      MessageFactory messageFactory,
                                      ServletHost servletHost) {
    	
    	this.endpoint = endpoint;
        this.component = (RuntimeComponent)endpoint.getComponent();
        this.service = (RuntimeComponentService)endpoint.getService();
        this.binding = (RESTBinding)endpoint.getBinding();
        
        this.extensionPoints = extensionPoints;
        this.messageFactory = messageFactory;
        this.servletHost = servletHost;
        
        // retrieve operation selector and wire format service providers
        
        ProviderFactoryExtensionPoint  providerFactories = extensionPoints.getExtensionPoint(ProviderFactoryExtensionPoint.class);

        
        if (binding.getOperationSelector() != null) {
            // Configure the interceptors for operation selection
            OperationSelectorProviderFactory osProviderFactory = (OperationSelectorProviderFactory) providerFactories.getProviderFactory(binding.getOperationSelector().getClass());
            if (osProviderFactory != null) {
                this.osProvider = osProviderFactory.createServiceOperationSelectorProvider(endpoint);
            }            
        }
        
        if (binding.getRequestWireFormat() != null && binding.getResponseWireFormat() != null) {
            // Configure the interceptors for wire format
            WireFormatProviderFactory wfProviderFactory = (WireFormatProviderFactory) providerFactories.getProviderFactory(binding.getRequestWireFormat().getClass());
            if (wfProviderFactory != null) {
                this.wfProvider = wfProviderFactory.createServiceWireFormatProvider(endpoint);
            }            
        }

        
        //clone the service contract to avoid databinding issues
        try {
            this.serviceContract = (InterfaceContract) service.getInterfaceContract().clone();
            
            // configure data binding
            if (this.wfProvider != null) {
                wfProvider.configureWireFormatInterfaceContract(serviceContract);
            }
        } catch(CloneNotSupportedException e) {
            this.serviceContract = service.getInterfaceContract();
        }
        
    }

    public void start() {
        // Get the invokers for the supported operations
        Servlet servlet = null;
        Invoker bindingInvoker = endpoint.getBindingInvocationChain().getHeadInvoker();
        bindingListenerServlet = new RESTBindingListenerServlet(binding, bindingInvoker, messageFactory );
        for (InvocationChain invocationChain : endpoint.getInvocationChains()) {
            
            Operation operation = invocationChain.getTargetOperation();
            Invoker serviceInvoker = invocationChain.getHeadInvoker();
            String operationName = operation.getName();
            
            if (binding.getOperationSelector() != null || binding.getRequestWireFormat() != null) {
                bindingListenerServlet.setInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("get")) { 
                bindingListenerServlet.setGetInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("conditionalGet")) {
                bindingListenerServlet.setConditionalGetInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("delete")) {
                bindingListenerServlet.setDeleteInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("conditionalDelete")) {
                bindingListenerServlet.setConditionalDeleteInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("put")) {
                bindingListenerServlet.setPutInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("conditionalPut")) {
                bindingListenerServlet.setConditionalPutInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("post")) {
                bindingListenerServlet.setPostInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("conditionalPost")) {
                bindingListenerServlet.setConditionalPostInvoker(serviceInvoker);
                servlet = bindingListenerServlet;
            } else if (operationName.equals("service")) {
                servlet = new RESTServiceListenerServlet(binding, serviceInvoker, messageFactory);
                break;
            }             
        }
        if (servlet == null) {
            throw new IllegalStateException("No get or service method found on the service");
        }
                
        // Create our HTTP service listener Servlet and register it with the
        // Servlet host
        servletMapping = binding.getURI();
        if (!servletMapping.endsWith("/")) {
            servletMapping += "/";
        }
        if (!servletMapping.endsWith("*")) {
            servletMapping += "*";
        }
        
        servletHost.addServletMapping(servletMapping, servlet);
    }

    public void stop() {        
        // Unregister the Servlet from the Servlet host
        servletHost.removeServletMapping(servletMapping);
    }

    public InterfaceContract getBindingInterfaceContract() {
        return serviceContract;
    }
    
    public boolean supportsOneWayInvocation() {
        return false;
    }
    
    /**
     * Add specific rest interceptor to invocation chain
     */
    public void configure() {

        InvocationChain bindingChain = endpoint.getBindingInvocationChain();

        if (wfProvider != null) {
            bindingChain.addInterceptor(Phase.SERVICE_BINDING_WIREFORMAT, wfProvider.createInterceptor());
        }
        
        if(osProvider != null) {
            bindingChain.addInterceptor(Phase.SERVICE_BINDING_OPERATION_SELECTOR, osProvider.createInterceptor());    
        }        

    }

}