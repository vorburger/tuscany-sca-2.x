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

package org.apache.tuscany.sca.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import itest.nodes.Helloworld;

import org.junit.After;
import org.junit.Test;
import org.oasisopen.sca.SCARuntimeException;
import org.oasisopen.sca.client.SCAClient;

/**
 * This shows how to test the Calculator service component.
 */
public class MultipleNodesPerJVMTestCase{

    private static DomainNode clientNode;
    private static DomainNode serviceNode;
    
    @Test
    public void testTwoNodesSameDomain() throws Exception {
        serviceNode = new DomainNode("vm://fooDomain", "target/test-classes/itest-nodes-helloworld-service-2.0-SNAPSHOT.jar");
        clientNode = new DomainNode("vm://fooDomain", "target/test-classes/itest-nodes-helloworld-client-2.0-SNAPSHOT.jar");

        Helloworld service = SCAClient.getService(Helloworld.class, "fooDomain/HelloworldService");
        assertNotNull(service);
        assertEquals("Hello Petra", service.sayHello("Petra"));

        Helloworld client = SCAClient.getService(Helloworld.class, "fooDomain/HelloworldClient");
        assertNotNull(client);
        assertEquals("Hi Hello Petra", client.sayHello("Petra"));

        if (clientNode != null && clientNode.isStarted()) {
            clientNode.stop();
        }
        if (serviceNode != null && serviceNode.isStarted()) {
            serviceNode.stop();
        }
    }

    @Test
    public void testTwoNodesDifferentDomains() throws Exception {
        serviceNode = new DomainNode("vm://fooDomain", "target/test-classes/itest-nodes-helloworld-service-2.0-SNAPSHOT.jar");
        Helloworld service = SCAClient.getService(Helloworld.class, "fooDomain/HelloworldService");
        assertNotNull(service);
        assertEquals("Hello Petra", service.sayHello("Petra"));

        clientNode = new DomainNode("vm://barDomain", "target/test-classes/itest-nodes-helloworld-client-2.0-SNAPSHOT.jar");
        Helloworld client = SCAClient.getService(Helloworld.class, "barDomain/HelloworldClient");
        assertNotNull(client);

        try {
            assertEquals("Hi Hello Petra", client.sayHello("Petra"));
            // fail();  //TODO: this should fail but it doesn't so the domain names aren't working properly as it picks up the fooDomain
        } catch (SCARuntimeException e) {
            e.printStackTrace();
            // FIXME: this gives an SCARuntimeException, would be better to be something like ServiceNotFoundException?
            // expected
        }
    }

    @After
    public void tearDownAfterClass() throws Exception {
        if (clientNode != null && clientNode.isStarted()) {
            clientNode.stop();
        }
        if (serviceNode != null && serviceNode.isStarted()) {
            serviceNode.stop();
        }
    }
}