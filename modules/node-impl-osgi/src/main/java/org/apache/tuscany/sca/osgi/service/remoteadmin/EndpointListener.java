/*
 *
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

package org.apache.tuscany.sca.osgi.service.remoteadmin;

/**
 * A whiteboard service that represents a listener for endpoints. An Endpoint
 * Listener represents a participant in the distributed model that is interested
 * in Endpoint Descriptions. This whiteboard service can be used in many
 * different scenarios. However, the primary use case is to allow a remote
 * controller to be informed of End Point Descriptions available in the network
 * and inform the network about available End Point Descriptions. Both the
 * network bundle and the controller bundle register a Endpoint Listener
 * service. The controller informs the network bundle about End Points that it
 * creates. The network bundles then uses a protocol like for example SLP to
 * announce these local end-points to the network. If the network bundle
 * discovers a new Endpoint through its discovery protocol, then it sends an End
 * Point Description to all the End Point Listener services that are registered
 * (except its own) that have specified an interest in that endpoint.
 * <p>
 * Endpoint Listener services can express their scope with the service property
 * ENDPOINT_LISTENER_SCOPE. This service property is a list of filters. An
 * Endpoint Description should only be given to a Endpoint Listener when there
 * is at least one filter that matches the Endpoint Description properties.
 * given to it. This filter model is quite flexible. For example, a discovery
 * bundle is only interested in locally originating Endpoint Descriptions. The
 * following filter ensure that it only sees local endpoints.
 * <p>
 * (org.osgi.framework.uuid=72dc5fd9-5f8f-4f8f-9821- 9ebb433a5b72)<br>
 * In the same vein, a controller that is only interested in remote Endpoint
 * Descriptions can use a filter like:
 * <p>
 * (!(org.osgi.framework.uuid=72dc5fd9-5f8f-4f8f-9821-9ebb433a5b72)) <br>
 * Where in both cases, the given UUID is the UUID of the local framework that
 * can be found in the Framework properties. The Endpoint Listener�s scope maps
 * very well to the service hooks. A controller can just register all filters
 * found from the Listener Hook as its scope. This will automatically provide it
 * with all known endpoints that match the given scope, without having to
 * inspect the filter string. In general, when an Endpoint Description is
 * discovered, it should be dispatched to all registered Endpoint Listener
 * services. If a new Endpoint Listener is registered, it should be informed
 * about all currently known Endpoints that match its scope. If a getter of the
 * Endpoint Listener service is unregistered, then all its registered Endpoint
 * Description objects must be removed. The Endpoint Listener models a best
 * effort approach. Participating bundles should do their utmost to keep the
 * listeners up to date, but implementers should realize that many endpoints
 * come through unreliable discovery processes.
 * 
 * @ThreadSafe
 */
public interface EndpointListener {
    /**
     * Specifies the interest of this listener with filters. This listener is
     * only interested in Endpoint Descriptions where its properties match the
     * given filter. The type of this property must be String+.
     */
    public static final String ENDPOINT_LISTENER_SCOPE = "endpoint.listener.scope";

    /**
     * Register an endpoint with this listener. If the endpoint matches one of
     * the filters registered with the ENDPOINT_LISTENER_SCOPE service property
     * then this filter should be given as the matchedFilter parameter. When
     * this service is first registered or it is modified, it should receive all
     * known endpoints matching the filter.
     * 
     * @param endpoint The Endpoint Description to be published
     * @param matchedFilter matchedFilter The filter from the
     *            ENDPOINT_LISTENER_SCOPE that matched the endpoint, must not be
     *            null.
     */
    public void addEndpoint(EndpointDescription endpoint, String matchedFilter);

    /**
     * Remove the registration of an endpoint. If an endpoint that was
     * registered with the addEndpoint method is no longer available then this
     * method should be called. This will remove the endpoint from the listener.
     * It is not necessary to remove endpoints when the service is unregistered
     * or modified in such a way that not all endpoints match the interest
     * filter anymore.
     * 
     * @param endpoint The Endpoint Description that is no longer valid.
     */
    public void removeEndpoint(EndpointDescription endpoint);
}