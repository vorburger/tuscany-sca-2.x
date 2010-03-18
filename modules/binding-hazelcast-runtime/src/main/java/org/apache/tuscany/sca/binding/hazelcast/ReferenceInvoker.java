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

package org.apache.tuscany.sca.binding.hazelcast;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import org.apache.tuscany.sca.common.xml.dom.DOMHelper;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.core.FactoryExtensionPoint;
import org.apache.tuscany.sca.endpoint.hazelcast.HazelcastEndpointRegistry;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.Invoker;
import org.apache.tuscany.sca.invocation.Message;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.runtime.DomainRegistryFactory;
import org.apache.tuscany.sca.runtime.EndpointRegistry;
import org.apache.tuscany.sca.runtime.ExtensibleDomainRegistryFactory;
import org.oasisopen.sca.ServiceRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.hazelcast.core.DistributedTask;
import com.hazelcast.core.Member;

public class ReferenceInvoker implements Invoker {

    HazelcastEndpointRegistry hzRegistry;
    DOMHelper domHelper;
    String serviceURI;
    private Operation operation;
    MessageFactory messageFactory;

    public ReferenceInvoker(ExtensionPointRegistry extensionsRegistry, String serviceURI, Operation operation) {
        this.serviceURI = serviceURI;
        this.operation = operation;
        DomainRegistryFactory domainRegistryFactory = ExtensibleDomainRegistryFactory.getInstance(extensionsRegistry);
        for (EndpointRegistry r : domainRegistryFactory.getEndpointRegistries()) {
            if (r instanceof HazelcastEndpointRegistry) {
                hzRegistry = (HazelcastEndpointRegistry)r;
                break;
            }
        }
        this.domHelper = DOMHelper.getInstance(extensionsRegistry);
        FactoryExtensionPoint modelFactories = extensionsRegistry.getExtensionPoint(FactoryExtensionPoint.class);
        this.messageFactory = modelFactories.getFactory(MessageFactory.class);
    }

    public Message invoke(Message msg) {
        Member owningMember = hzRegistry.getOwningMember(serviceURI);
        String requestXML = getRequestXML(msg);
        Callable<String> callable = new ServiceInvoker(serviceURI, operation.getName(), requestXML);
        FutureTask<String> task = new DistributedTask<String>(callable, owningMember);
        ExecutorService executorService = hzRegistry.getHazelcastInstance().getExecutorService();
        executorService.execute(task);
        try {
            return getResponseNode(task.get());
        } catch (Exception e) {
            throw new ServiceRuntimeException(e);
        }
    }

    private String getRequestXML(Message msg) {
        Object[] args = msg.getBody();
        String msgXML = domHelper.saveAsString((Node)args[0]);
        return msgXML;
    }

    private Message getResponseNode(String responseXML) throws IOException, SAXException {
        Document responseDOM = domHelper.load(responseXML);
        Message msg = messageFactory.createMessage();
        msg.setBody(responseDOM);
        return msg;
    }

}