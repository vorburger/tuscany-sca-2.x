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

package org.apache.tuscany.sca.binding.jsonp;

import javax.xml.namespace.QName;

import org.apache.tuscany.sca.binding.http.HTTPDefaultOperationSelector;
import org.apache.tuscany.sca.binding.http.HTTPDefaultWireFormat;
import org.apache.tuscany.sca.binding.http.impl.HTTPBindingImpl;

/**
 * JSONP Binding model
 */
public class JSONPBinding extends HTTPBindingImpl {

    public static final QName TYPE = new QName(SCA11_TUSCANY_NS, "binding.jsonp");

    public JSONPBinding() {
        super();

        // configure the HTTP binding for JSONP (which for the moment is the default wireFormat)
        setOperationSelector(new HTTPDefaultOperationSelector());
        setRequestWireFormat(new HTTPDefaultWireFormat());
        setResponseWireFormat(new HTTPDefaultWireFormat());
    }

    @Override
    public QName getType() {
        return TYPE;
    }
}
