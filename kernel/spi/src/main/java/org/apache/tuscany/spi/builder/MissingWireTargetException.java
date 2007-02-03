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
package org.apache.tuscany.spi.builder;

import java.net.URI;

/**
 * Denotes a missing wire target in an assembly
 *
 * @version $Rev$ $Date$
 */
public class MissingWireTargetException extends WiringException {

    public MissingWireTargetException(String message) {
        super(message);
    }

    public MissingWireTargetException(String message, URI sourceUri, URI targetUri) {
        super(message, sourceUri, targetUri);
    }
}
