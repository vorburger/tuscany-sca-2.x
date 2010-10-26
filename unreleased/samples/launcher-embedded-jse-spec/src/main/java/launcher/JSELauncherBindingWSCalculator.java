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

package launcher;

import org.apache.tuscany.sca.node.Contribution;
import org.apache.tuscany.sca.node2.Node;
import org.apache.tuscany.sca.node2.NodeFactory;

import calculator.CalculatorService;

/**
 * This client program shows how to create an embedded SCA runtime, load a contribution,
 * start it and locate and invoke an SCA component 
 */
public class JSELauncherBindingWSCalculator {
    
    public static void main(String[] args) throws Exception {
        JSELauncherBindingWSCalculator launcher = new JSELauncherBindingWSCalculator();
        launcher.launchBindingWSCalculator();         
    }
    
    /*
     * Using the Tuscany Node API to load a contribution.
     * Using the Tuscany Node API to get a local service proxy
     */
    public void launchBindingWSCalculator(){
        NodeFactory nodeFactory = NodeFactory.newInstance();
        Node node = nodeFactory.createNode("MyDomain");
        
        try {
            node.installContribution("../../../samples/contribution-binding-ws-calculator/target/sample-contribution-binding-ws-calculator.jar");
        } catch(Exception ex){
            System.out.println("Exception on installContribution");
            ex.printStackTrace();
        }
        
        //node.start();
        
        CalculatorService calculator = null;
        
        try {        
            calculator = node.getService(CalculatorService.class, "CalculatorServiceComponent");
        } catch(Exception ex){
            System.out.println("Exception on getService");
            ex.printStackTrace();
        }
        
        // TODO - could use JUnit assertions but don't want to have to handle JUnit dependency from Ant script
        double result = calculator.add(3, 2);
        System.out.println("3 + 2 = " + result);
        if (result != 5.0){
            throw new SampleLauncherException();
        }
        
        node.stop();
    }
    
}
