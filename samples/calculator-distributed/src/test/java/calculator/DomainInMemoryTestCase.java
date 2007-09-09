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
package calculator;


import junit.framework.Assert;

import org.apache.tuscany.sca.distributed.node.impl.NodeImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import calculator.CalculatorService;

/**
 * Runs a distributed domain in a single VM by using and in memory 
 * implementation of the distributed domain
 */
public class DomainInMemoryTestCase {
    
    private static String DEFAULT_DOMAIN_NAME = "mydomain";

    private static NodeImpl registry;
    private static NodeImpl domainNodeA;
    private static NodeImpl domainNodeB;
    private static NodeImpl domainNodeC;

    private static CalculatorService calculatorServiceA;

    @BeforeClass
    public static void init() throws Exception {
        
        try {
                System.out.println("Setting up domain registry");
                
                registry = new NodeImpl();
                registry.start();
                registry.getContributionManager().startContribution(DomainInMemoryTestCase.class.getClassLoader().getResource("domain/"));
                
                System.out.println("Setting up distributed nodes");
                       
                // Create the domain representation
                domainNodeA = new NodeImpl(DEFAULT_DOMAIN_NAME, "nodeA");
                domainNodeA.start();
                domainNodeA.getContributionManager().startContribution(DomainInMemoryTestCase.class.getClassLoader().getResource("nodeA/"));
                
                // Create the domain representation
                domainNodeB = new NodeImpl(DEFAULT_DOMAIN_NAME, "nodeB");
                domainNodeB.start();
                domainNodeB.getContributionManager().startContribution(DomainInMemoryTestCase.class.getClassLoader().getResource("nodeB/"));        
                
                // create the node that runs the 
                // subtract component 
                domainNodeC = new NodeImpl(DEFAULT_DOMAIN_NAME, "nodeC");
                domainNodeC.start();
                domainNodeC.getContributionManager().startContribution(DomainInMemoryTestCase.class.getClassLoader().getResource("nodeC/"));         
        
                // get a reference to the calculator service from domainA
                // which will be running this component
                calculatorServiceA = domainNodeA.getService(CalculatorService.class, "CalculatorServiceComponent");
        } catch(Exception ex){
                System.err.println(ex.toString());
        }
   }

    @AfterClass
    public static void destroy() throws Exception {
        // stop the domain and hence the nodes it contains  
        domainNodeA.stop();
        domainNodeB.stop();
        domainNodeC.stop();
    }

    @Test
    public void testCalculator() throws Exception {       
        
        // Calculate
        Assert.assertEquals(calculatorServiceA.add(3, 2), 5.0);
        Assert.assertEquals(calculatorServiceA.subtract(3, 2), 1.0);
        Assert.assertEquals(calculatorServiceA.multiply(3, 2), 6.0);
        Assert.assertEquals(calculatorServiceA.divide(3, 2), 1.5);
    }
}
