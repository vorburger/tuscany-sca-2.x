/**
 *  Copyright 2005 The Apache Software Foundation or its licensors, as applicable.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.tuscany.core.implementation.composite;

import java.lang.reflect.Method;

import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.component.WorkContext;
import org.apache.tuscany.spi.extension.ReferenceExtension;
import org.apache.tuscany.spi.idl.java.JavaIDLUtils;
import org.apache.tuscany.spi.model.Operation;
import org.apache.tuscany.spi.model.ServiceContract;
import org.apache.tuscany.spi.wire.TargetInvoker;
import org.apache.tuscany.spi.wire.WireService;

import org.apache.tuscany.core.injection.WireObjectFactory;

public class CompositeReference extends ReferenceExtension {

    private WorkContext workContext;

    public CompositeReference(String name,
                              CompositeComponent parent,
                              WireService wireService,
                              ServiceContract contract,
                              WorkContext workContext) {
        super(name, contract.getInterfaceClass(), parent, wireService);
        this.workContext = workContext;
    }

    public TargetInvoker createTargetInvoker(ServiceContract contract, Operation operation) {
        WireObjectFactory wireFactory = new WireObjectFactory(outboundWire, wireService);
        Method method = JavaIDLUtils.findMethod(operation, contract.getInterfaceClass().getMethods());
        return new CompositeReferenceTargetInvoker(method, inboundWire, wireFactory, workContext);
    }

    public TargetInvoker createCallbackTargetInvoker(ServiceContract contract, Operation operation) {
        Method method = JavaIDLUtils.findMethod(operation, contract.getCallbackClass().getMethods());
        return new CompositeReferenceCallbackTargetInvoker(method, contract, inboundWire, wireService, workContext);
    }
}
