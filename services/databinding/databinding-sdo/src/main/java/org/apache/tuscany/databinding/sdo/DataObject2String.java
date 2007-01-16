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
package org.apache.tuscany.databinding.sdo;

import javax.xml.namespace.QName;

import org.apache.tuscany.spi.databinding.PullTransformer;
import org.apache.tuscany.spi.databinding.TransformationContext;
import org.apache.tuscany.spi.databinding.TransformationException;
import org.apache.tuscany.spi.databinding.Transformer;
import org.apache.tuscany.spi.databinding.extension.TransformerExtension;
import org.osoa.sca.annotations.Service;

import commonj.sdo.DataObject;
import commonj.sdo.helper.HelperContext;
import commonj.sdo.helper.XMLHelper;

@Service(Transformer.class)
public class DataObject2String extends TransformerExtension<DataObject, String> implements
        PullTransformer<DataObject, String> {

    private static final String TUSCANY_SDO = "http://tuscany.apache.org/xmlns/sdo/1.0-SNAPSHOT";

    public String transform(DataObject source, TransformationContext context) {
        try {
            HelperContext helperContext = SDODataTypeHelper.getHelperContext(context);
            XMLHelper xmlHelper = helperContext.getXMLHelper();
            Object logicalType = context.getSourceDataType().getLogical();
            if (logicalType instanceof QName) {
                QName elementName = (QName) logicalType;
                return xmlHelper.save(source, elementName.getNamespaceURI(), elementName.getLocalPart());
            } else {
                return xmlHelper.save(source, TUSCANY_SDO, "dataObject");
            }
        } catch (Exception e) {
            throw new TransformationException(e);
        }
    }

    public Class getSourceType() {
        return DataObject.class;
    }

    public Class getTargetType() {
        return String.class;
    }

    public int getWeight() {
        return 40;
    }

}
