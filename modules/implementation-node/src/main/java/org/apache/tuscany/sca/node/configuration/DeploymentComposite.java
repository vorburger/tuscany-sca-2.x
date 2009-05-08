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

package org.apache.tuscany.sca.node.configuration;

/**
 * Configuration for a deployment composite
 */
public interface DeploymentComposite {
    /**
     * Get the location of the deployment composite, it can be relative to the owning
     * contribution or an external resource
     * @return
     */
    String getLocation();

    /**
     * Set the location of the deployment composite
     * @param location
     */
    void setLocation(String location);

    /**
     * Get string content of the deployment composite (XML)
     * @return
     */
    String getContent();

    void setContent(String compositeXML);

    /**
     * Get the URI of the owning contribution
     * @return
     */
    String getContributionURI();

    /**
     * Set the URI of the owning contribution
     * @param contributionURI
     */
    void setContributionURI(String contributionURI);

}
