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

package org.apache.tuscany.sca.binding.jms.wireformat.jmstext;

import java.util.List;

import org.apache.tuscany.sca.assembly.Binding;
import org.apache.tuscany.sca.binding.jms.impl.JMSBinding;
import org.apache.tuscany.sca.binding.jms.impl.JMSBindingConstants;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.interfacedef.InterfaceContract;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.Interceptor;
import org.apache.tuscany.sca.invocation.Phase;
import org.apache.tuscany.sca.policy.PolicySet;
import org.apache.tuscany.sca.policy.PolicySetAttachPoint;
import org.apache.tuscany.sca.policy.util.PolicyHandler;
import org.apache.tuscany.sca.provider.PolicyProvider;
import org.apache.tuscany.sca.provider.WireFormatProvider;
import org.apache.tuscany.sca.runtime.RuntimeComponent;
import org.apache.tuscany.sca.runtime.RuntimeComponentService;

/**
 * @version $Rev$ $Date$
 */
public class WireFormatJMSTextServiceProvider implements WireFormatProvider {
    private ExtensionPointRegistry registry;
    private RuntimeComponent component;
    private RuntimeComponentService service;
    private JMSBinding binding;
    private InterfaceContract interfaceContract; 

    public WireFormatJMSTextServiceProvider(ExtensionPointRegistry registry,
                                             RuntimeComponent component, 
                                             RuntimeComponentService service, 
                                             Binding binding) {
        super();
        this.registry = registry;
        this.component = component;
        this.service = service;
        this.binding = (JMSBinding)binding;
        
        // configure the service based on this wire format
        
        // currently maintaining the message processor structure which 
        // contains the details of jms message processing however overried 
        // any message processors specied in the SCDL in this case
        this.binding.setRequestMessageProcessorName(JMSBindingConstants.TEXT_MP_CLASSNAME);
        this.binding.setResponseMessageProcessorName(JMSBindingConstants.TEXT_MP_CLASSNAME);
        
        // just point to the reference interface contract so no 
        // databinding transformation takes place
        interfaceContract = service.getInterfaceContract();
    }
    
    public InterfaceContract getWireFormatInterfaceContract() {
        return interfaceContract;
    }

    /**
     */
    public Interceptor createInterceptor() {
        return new WireFormatJMSTextServiceInterceptor((JMSBinding)binding,
                                                         null,
                                                        service.getRuntimeWire(binding));
    }

    /**
     */
    public String getPhase() {
        return Phase.SERVICE_BINDING_WIREFORMAT;
    }

}
