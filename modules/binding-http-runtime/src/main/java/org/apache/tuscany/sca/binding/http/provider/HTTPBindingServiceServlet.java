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

package org.apache.tuscany.sca.binding.http.provider;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tuscany.sca.invocation.Message;
import org.apache.tuscany.sca.invocation.MessageFactory;
import org.apache.tuscany.sca.runtime.RuntimeEndpoint;

public class HTTPBindingServiceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected transient MessageFactory messageFactory;
    protected transient RuntimeEndpoint wire;
    
    public HTTPBindingServiceServlet(RuntimeEndpoint wire, MessageFactory messageFactory) {
        this.wire = wire;
        this.messageFactory = messageFactory;
    }
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HTTPContext bindingContext = new HTTPContext();
        bindingContext.setRequest(request);
        bindingContext.setResponse(response);
        Message msg = messageFactory.createMessage();
        msg.setBindingContext(bindingContext);
        wire.invoke(msg);
    }    
}
