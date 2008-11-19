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

package org.apache.tuscany.sca.binding.corba.testing.generated;

/**
* org/apache/tuscany/sca/binding/corba/testing/generated/ArraysSetterHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from general_tests.idl
* monday, 23 june 2008 14:12:28 CEST
*/

abstract public class ArraysSetterHelper {
    private static String _id = "IDL:org/apache/tuscany/sca/binding/corba/testing/generated/ArraysSetter:1.0";

    public static void insert(org.omg.CORBA.Any a,
                              org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            __typeCode =
                org.omg.CORBA.ORB
                    .init()
                    .create_interface_tc(org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetterHelper.id(),
                                         "ArraysSetter");
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter read(org.omg.CORBA.portable.InputStream istream) {
        return narrow(istream.read_Object(_ArraysSetterStub.class));
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream,
                             org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter value) {
        ostream.write_Object((org.omg.CORBA.Object)value);
    }

    public static org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter narrow(org.omg.CORBA.Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter)
            return (org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter)obj;
        else if (!obj._is_a(id()))
            throw new org.omg.CORBA.BAD_PARAM();
        else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate();
            org.apache.tuscany.sca.binding.corba.testing.generated._ArraysSetterStub stub =
                new org.apache.tuscany.sca.binding.corba.testing.generated._ArraysSetterStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

    public static org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter unchecked_narrow(org.omg.CORBA.Object obj) {
        if (obj == null)
            return null;
        else if (obj instanceof org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter)
            return (org.apache.tuscany.sca.binding.corba.testing.generated.ArraysSetter)obj;
        else {
            org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate();
            org.apache.tuscany.sca.binding.corba.testing.generated._ArraysSetterStub stub =
                new org.apache.tuscany.sca.binding.corba.testing.generated._ArraysSetterStub();
            stub._set_delegate(delegate);
            return stub;
        }
    }

}
