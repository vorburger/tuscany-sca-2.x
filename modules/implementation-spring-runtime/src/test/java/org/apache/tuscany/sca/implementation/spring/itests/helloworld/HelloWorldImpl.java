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

package org.apache.tuscany.sca.implementation.spring.itests.helloworld;

/**
 * A simple proxy Java class which implements the HelloWorld interface but which uses
 * a reference "delegate" to actually provide the HelloWorld service
 *
 * @version $Rev$ $Date$
 */
public class HelloWorldImpl implements HelloWorld {

    static String hello = "Hello ";

    public String sayHello(String s) {
        // Simply call the reference to satisfy the service request...
        System.out.println("HelloWorldImpl - sayHello called");
        return (hello + s);
    }

}
