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
package org.apache.tuscany.core.wire;

import org.apache.tuscany.spi.wire.Interceptor;
import org.apache.tuscany.spi.wire.Message;
import org.apache.tuscany.spi.wire.MessageChannel;

/**
 * Bridges interceptors between a {@link org.apache.tuscany.spi.wire.InboundInvocationChain} and a
 * {@link org.apache.tuscany.spi.wire.OutboundInvocationChain}.
 *
 * @version $$Rev$$ $$Date$$
 */
public class BridgingInterceptor implements Interceptor {
    private Interceptor next;
    private MessageChannel responseChannel;

    public BridgingInterceptor() {
    }

    public BridgingInterceptor(Interceptor next, MessageChannel responseChannel) {
        this.next = next;
        this.responseChannel = responseChannel;
    }

    public BridgingInterceptor(Interceptor next) {
        this.next = next;
    }

    public Message invoke(Message msg) {
        Message response = next.invoke(msg);
        if (responseChannel != null) {
            responseChannel.send(response);
        }
        return response;
    }

    public Interceptor getNext() {
        return next;
    }

    public void setNext(Interceptor next) {
        this.next = next;
    }

    public boolean isOptimizable() {
        return true;
    }
}
