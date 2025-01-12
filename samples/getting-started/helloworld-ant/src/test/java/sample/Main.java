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
package sample;

import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;
import org.oasisopen.sca.NoSuchServiceException;

public class Main {

    public static void main(String[] args) throws NoSuchServiceException {

        // Run the SCA composite in a Tuscany runtime
        Node node = TuscanyRuntime.runComposite("helloworld.composite", "target/classes");
        try {
            
            // Get the Helloworld service proxy
            Helloworld helloworld = node.getService(Helloworld.class, "HelloworldComponent");
            
            // Call the sayHello method
            System.out.println(helloworld.sayHello(args.length>0 ? args[0]: "World"));
            
        } finally {
            // Stop the Tuscany runtime Node
            node.stop();        
        }
    }
}
