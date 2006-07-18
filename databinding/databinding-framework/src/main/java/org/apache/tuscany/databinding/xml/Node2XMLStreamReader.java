/**
 *
 * Copyright 2006 The Apache Software Foundation or its licensors as applicable
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
package org.apache.tuscany.databinding.xml;

import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;

import org.apache.tuscany.databinding.PullTransformer;
import org.apache.tuscany.databinding.TransformationContext;
import org.apache.tuscany.databinding.TransformationException;
import org.w3c.dom.Node;

import com.ctc.wstx.api.ReaderConfig;
import com.ctc.wstx.dom.DOMWrappingReader;

/**
 * Transform DOM Node to XML XMLStreamReader
 * 
 */
public class Node2XMLStreamReader implements PullTransformer<Node, XMLStreamReader> {

    public XMLStreamReader transform(Node source, TransformationContext context) {
        try {
            DOMSource domSource = new DOMSource(source);
            ReaderConfig config = ReaderConfig.createFullDefaults();
            DOMWrappingReader wrappingReader = DOMWrappingReader.createFrom(config, domSource);
            return wrappingReader;
        } catch (Exception e) {
            throw new TransformationException(e);
        }
    }

    public Class<Node> getSourceType() {
        return Node.class;
    }

    public Class<XMLStreamReader> getTargetType() {
        return XMLStreamReader.class;
    }

    public int getWeight() {
        return 40;
    }

}
