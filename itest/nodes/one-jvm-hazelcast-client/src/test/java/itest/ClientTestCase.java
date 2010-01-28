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

package itest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.apache.tuscany.sca.core.assembly.impl.RuntimeEndpointImpl;
import org.apache.tuscany.sca.node.Node;
import org.apache.tuscany.sca.node.NodeFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 */
public class ClientTestCase{

    private static URI domainURI = URI.create("tuscany:OneNodeTestCase");
    private static Node node;
    private static HazelcastInstance client;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        node = NodeFactory.getInstance().createNode(domainURI, "../helloworld-service/target/classes", "../helloworld-client/target/classes");
        node.start();
    }

    @Test
    public void testNode() throws Exception {
        
        client = HazelcastClient.newHazelcastClient("OneNodeTestCase", "tuscany", "192.168.1.73:14820"); 
        IMap<Object, Object> map = client.getMap("OneNodeTestCase/Endpoints");        

        assertNotNull(map);
        assertEquals(2, map.size());
        Object ep1 = map.get("HelloworldService#service-binding(Helloworld/Helloworld)");
        System.out.println((RuntimeEndpointImpl)ep1);
        assertNotNull(ep1);
        assertTrue(ep1 instanceof RuntimeEndpointImpl);
        Object ep2 = map.get("HelloworldClient#service-binding(Helloworld/Helloworld)");
        assertNotNull(ep2);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (client != null) {
            client.shutdown();
        }
        if (node != null) {
            node.stop();
        }
    }
}
