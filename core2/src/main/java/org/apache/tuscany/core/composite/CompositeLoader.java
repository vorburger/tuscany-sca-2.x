/**
 *
 * Copyright 2005 The Apache Software Foundation
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
package org.apache.tuscany.core.composite;

import javax.xml.namespace.QName;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.extension.LoaderExtension;
import org.apache.tuscany.spi.loader.LoaderException;
import org.apache.tuscany.spi.loader.LoaderRegistry;
import org.apache.tuscany.spi.model.ComponentDefinition;
import org.apache.tuscany.spi.model.CompositeComponentType;
import org.apache.tuscany.spi.model.Implementation;
import org.apache.tuscany.spi.model.ModelObject;
import org.apache.tuscany.spi.model.Property;
import org.apache.tuscany.spi.model.ReferenceDefinition;
import org.apache.tuscany.spi.model.ServiceDefinition;

import org.apache.tuscany.core.loader.AssemblyConstants;

/**
 * Loads a composite component definition from an XML-based assembly file
 *
 * @version $Rev$ $Date$
 */
public class CompositeLoader extends LoaderExtension<CompositeComponentType> {
    public CompositeLoader() {
    }

    public CompositeLoader(LoaderRegistry registry) {
        super(registry);
    }

    protected QName getXMLType() {
        return AssemblyConstants.COMPOSITE;
    }

    public CompositeComponentType load(XMLStreamReader reader,
                                       DeploymentContext deploymentContext)
        throws XMLStreamException, LoaderException {
        CompositeComponentType composite = new CompositeComponentType();
        composite.setName(reader.getAttributeValue(null, "name"));
        while (true) {
            switch (reader.next()) {
                case START_ELEMENT:
                    ModelObject o = registry.load(reader, deploymentContext);
                    if (o instanceof ServiceDefinition) {
                        composite.add((ServiceDefinition) o);
                    } else if (o instanceof ReferenceDefinition) {
                        composite.add((ReferenceDefinition) o);
                    } else if (o instanceof Property<?>) {
                        composite.add((Property<?>) o);
                    } else if (o instanceof ComponentDefinition<?>) {
                        composite.add((ComponentDefinition<? extends Implementation>) o);
                    }
                    reader.next();
                    break;
                case END_ELEMENT:
                    return composite;
            }
        }
    }
}
