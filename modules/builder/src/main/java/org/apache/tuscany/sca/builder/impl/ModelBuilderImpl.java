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
package org.apache.tuscany.sca.builder.impl;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.tuscany.sca.assembly.Composite;
import org.apache.tuscany.sca.assembly.builder.CompositeBuilder;
import org.apache.tuscany.sca.assembly.builder.CompositeBuilderException;
import org.apache.tuscany.sca.assembly.builder.DeployedCompositeBuilder;
import org.apache.tuscany.sca.core.ExtensionPointRegistry;
import org.apache.tuscany.sca.definitions.Definitions;
import org.apache.tuscany.sca.monitor.Monitor;

public class ModelBuilderImpl implements CompositeBuilder, DeployedCompositeBuilder {
    private CompositeBuilder compositeIncludeBuilder;
    private CompositeBuilder compositeCloneBuilder;
    private CompositeComponentTypeBuilderImpl compositeComponentTypeBuilder;
    private ComponentBuilderImpl componentBuilder;

    private CompositeBuilder structuralURIBuilder;
    private BindingURIBuilderImpl bindingURIBuilder;
    private ComponentServiceBindingBuilderImpl componentServiceBindingBuilder;
    private ComponentReferenceBindingBuilderImpl componentReferenceBindingBuilder;
    private EndpointBuilderImpl endpointBuilder;
    private EndpointReferenceBuilderImpl endpointReferenceBuilder;
    private ComponentReferencePromotionBuilderImpl componentReferencePromotionBuilder;
    
    private CompositeBuilder policyAttachmentBuilder;
    private CompositePolicyBuilderImpl compositePolicyBuilder;

    /**
     * Constructs a new composite builder.
     * 
     * @param registry the extension point registry
     */
    public ModelBuilderImpl(ExtensionPointRegistry registry) {

        compositeIncludeBuilder = new CompositeIncludeBuilderImpl();
        compositeCloneBuilder = new CompositeCloneBuilderImpl();

        compositeComponentTypeBuilder = new CompositeComponentTypeBuilderImpl(registry);
        componentBuilder = new ComponentBuilderImpl(registry);

        compositeComponentTypeBuilder.setComponentBuilder(componentBuilder);
        componentBuilder.setComponentTypeBuilder(compositeComponentTypeBuilder);

        structuralURIBuilder = new StructuralURIBuilderImpl(registry);
        bindingURIBuilder = new BindingURIBuilderImpl(registry);
        componentServiceBindingBuilder = new ComponentServiceBindingBuilderImpl(registry);
        componentReferenceBindingBuilder = new ComponentReferenceBindingBuilderImpl(registry);
        endpointBuilder = new EndpointBuilderImpl(registry);
        endpointReferenceBuilder = new EndpointReferenceBuilderImpl(registry);
        componentReferencePromotionBuilder = new ComponentReferencePromotionBuilderImpl(registry);

        policyAttachmentBuilder = new PolicyAttachmentBuilderImpl(registry);
        compositePolicyBuilder = new CompositePolicyBuilderImpl(registry);
    }

    public String getID() {
        return "org.apache.tuscany.sca.assembly.builder.CompositeBuilder";
    }

    public Composite build(Composite composite, Definitions definitions, Monitor monitor)
        throws CompositeBuilderException {
        return build(composite, definitions, null, monitor);
    }

    public Composite build(Composite composite,
                           Definitions definitions,
                           Map<QName, List<String>> bindingBaseURIs,
                           Monitor monitor) throws CompositeBuilderException {

        try {
            // Clone the composites that are included or referenced in implementation.composite
            composite = compositeCloneBuilder.build(composite, definitions, monitor);

            // Collect and fuse includes. Copy all of the components
            // out of the included composite into the including composite
            // and discards the included composite
            composite = compositeIncludeBuilder.build(composite, definitions, monitor);

            // Set up the structural URIs for components (services/references/bindings?)
            composite = structuralURIBuilder.build(composite, definitions, monitor);
            
            // need to apply policy external attachment
            composite = policyAttachmentBuilder.build(composite, definitions, monitor);

            // Process the implementation hierarchy by calculating the component type 
            // for the top level implementation (composite). This has the effect of
            // recursively calculating component types and configuring the 
            // components that depend on them
            compositeComponentTypeBuilder.createComponentType(null, composite);

            // create the runtime model by updating the static model we have just 
            // created. This involves things like creating
            //  component URIs
            //  binding URIs
            //  binding specific build processing
            //  callback references - currently done in static pass
            //  callback services - currently done in static pass
            //  Endpoints
            //  Endoint References
            //  Policies
            // TODO - called here at the moment but we could have a separate build phase 
            //        to call these. Also we need to re-org these builders 
            composite = bindingURIBuilder.build(composite, definitions, monitor);
            composite = componentServiceBindingBuilder.build(composite, definitions, monitor); // binding specific build
            composite = componentReferenceBindingBuilder.build(composite, definitions, monitor); // binding specific build
            endpointBuilder.build(composite, definitions, monitor);
            endpointReferenceBuilder.build(composite, definitions, monitor);
            composite = componentReferencePromotionBuilder.build(composite, definitions, monitor); // move into the static build?
            composite = compositePolicyBuilder.build(composite, definitions, monitor); // the rest of the policy processing?
            return composite;
        } catch (Exception e) {
            throw new CompositeBuilderException("Exception while building model " + composite.getName(), e);
        }
    }
}