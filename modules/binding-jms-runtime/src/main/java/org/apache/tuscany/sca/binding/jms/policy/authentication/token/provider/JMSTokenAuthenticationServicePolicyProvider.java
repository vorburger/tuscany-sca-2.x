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

package org.apache.tuscany.sca.binding.jms.policy.authentication.token.provider;

import java.util.List;

import org.apache.tuscany.sca.assembly.Endpoint;
import org.apache.tuscany.sca.binding.jms.policy.authentication.token.JMSTokenAuthenticationPolicy;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.Phase;
import org.apache.tuscany.sca.invocation.PhasedInterceptor;
import org.apache.tuscany.sca.policy.PolicySet;
import org.apache.tuscany.sca.provider.PolicyProvider;

/**
 * @version $Rev$ $Date$
 */
public class JMSTokenAuthenticationServicePolicyProvider implements PolicyProvider {

    private Endpoint endpoint;

    public JMSTokenAuthenticationServicePolicyProvider(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    private PolicySet findPolicySet() {
        List<PolicySet> policySets = endpoint.getPolicySets();
        for (PolicySet ps : policySets) {
            for (Object p : ps.getPolicies()) {
                if (JMSTokenAuthenticationPolicy.class.isInstance(p)) {
                    return ps;
                }
            }
        }
        return null;
    }

    private String getContext() {
        return "component.service: " + endpoint.getComponent().getURI()
            + "#"
            + endpoint.getService().getName()
            + "("
            + endpoint.getBinding().getClass().getName()
            + ")";
    }

    /**
     * @see org.apache.tuscany.sca.provider.PolicyProvider#createInterceptor(org.apache.tuscany.sca.interfacedef.Operation)
     */
    public PhasedInterceptor createInterceptor(Operation operation) {
        PolicySet ps = findPolicySet();
        return ps == null ? null : new JMSTokenAuthenticationServicePolicyInterceptor(getContext(), ps, getPhase());
    }   

    /**
     * @see org.apache.tuscany.sca.provider.PolicyProvider#getPhase()
     */
    public String getPhase() {
        return Phase.SERVICE_BINDING_POLICY;
    }

    public void start() {
    }

    public void stop() {
    }

}