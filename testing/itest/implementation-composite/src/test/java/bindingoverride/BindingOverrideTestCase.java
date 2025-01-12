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
package bindingoverride;

import java.io.File;

import junit.framework.Assert;


import org.apache.tuscany.sca.node.Node;
import org.apache.tuscany.sca.node.NodeFactory;
import org.apache.tuscany.sca.node.Contribution;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import policy.Target;

public class BindingOverrideTestCase{

    private Node node;
    private Target targetClient;

    @Before
    public void setUp() throws Exception {
        try {
            NodeFactory factory = NodeFactory.newInstance();
            node = factory.createNode(new File("src/main/resources/bindingoverride/OuterComposite.composite").toURI().toURL().toString(),
                    new Contribution("TestContribution", new File("src/main/resources/bindingoverride/").toURI().toURL().toString()));
            node.start();
            targetClient = node.getService(Target.class, "OuterClientComponent");
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    @After
    public void tearDown() throws Exception {
        node.stop();
    }

    @Test
    public void test() throws Exception {
        Assert.assertEquals("Target: Hello Target: Hello Fred!!", targetClient.hello("Fred"));
    }
}
