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

package org.apache.tuscany.sca.implementation.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tuscany.sca.assembly.ComponentReference;
import org.apache.tuscany.sca.runtime.RuntimeComponent;

/**
 * Servlet that handles the GET request for the componentContext.js script
 * 
 * The script is generated by calling ScriptProcessors to output the code 
 * for the SCA references, properties etc.
 */
public class ComponentContextServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected static final String HEADER = "/** --- Apache Tuscany componentContext.js --- */\n";
    protected static final String FOOTER = "/** --- Apache Tuscany componentContext.js EOF --- */\n";

    public static final String COMPONENT_CONTEXT_SCRIPT_URI = "org.apache.tuscany.sca.componentContext.js";
    
    protected transient Map<String, Object> attributes = new HashMap<String, Object>();
    protected transient ServletContext servletContext;
    private transient List<ContextScriptProcessor> contextScriptProcessors = new ArrayList<ContextScriptProcessor>();

    public ComponentContextServlet() {
        contextScriptProcessors.add(new JSONRPCScripProcessor());
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        this.servletContext = servletConfig.getServletContext();
        if (attributes.size() > 0) {
            for (String name : attributes.keySet()) {
                servletContext.setAttribute(name, attributes.get(name));
            }
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException  {
        response.setContentType("text/plain;charset=utf-8");
        PrintWriter out = response.getWriter();

        out.write(HEADER);

        doScriptInit(req, response);

        doScriptReferences(req, response);

        out.write(FOOTER);

        out.flush();
        out.close();
    }

    /**
     * Calls each ContextScriptProcessor once to insert any required initilization code into componentContext.js  
     */
    protected void doScriptInit(HttpServletRequest req, HttpServletResponse response) throws IOException, UnsupportedEncodingException {

        PrintWriter out = response.getWriter();
        
        out.println("if (SCA == undefined) var SCA = new Object();");
        out.println("if (SCA.componentContext == undefined) {");
        out.println("   SCA.componentContext = new Object();");
        out.println("   SCA.componentContext.serviceNames = [];");
        out.println("   SCA.componentContext.serviceProxys = [];");
        out.println("   SCA.componentContext.getService = function(serviceName){");
        out.println("      var i = SCA.componentContext.serviceNames.indexOf(serviceName);");
        out.println("      return SCA.componentContext.serviceProxys[i];");
        out.println("   };");
        out.println("   if (componentContext == undefined) var componentContext = SCA.componentContext;");
        out.println("}");

        for (ContextScriptProcessor csp : contextScriptProcessors) {
            csp.scriptInit(req, response);
        }
    }

    /**
     * Calls each ContextScriptProcessor for each SCA reference to insert code for the reference into componentContext.js  
     */
    protected void doScriptReferences(HttpServletRequest req, HttpServletResponse response) throws IOException, UnsupportedEncodingException {

        PrintWriter out = response.getWriter();

        out.write("// SCA References\n");
        
        RuntimeComponent component = (RuntimeComponent)servletContext.getAttribute("org.apache.tuscany.sca.implementation.web.RuntimeComponent");
        
        for (ComponentReference cr : component.getReferences()) {
            String ref = "// SCA Reference " + cr.getName() + "\n";
            out.write(ref);
            for (ContextScriptProcessor csp : contextScriptProcessors) {
                csp.scriptReference(cr, req, response);
            }
        }

        out.write("\n// SCA References end.\n");
    }

    /**
     * Calls each ContextScriptProcessor for each SCA property to insert code for the property into componentContext.js  
     */
    protected void doScriptProperties(HttpServletRequest req, HttpServletResponse response) throws IOException, UnsupportedEncodingException {
        // TODO: support properties
    }

    /**
     * Set an attribute on the ServletContext
     */
    public void setAttribute(String name, Object value) {
        if (servletContext != null) {
            servletContext.setAttribute(name, value);
        } else {
            attributes.put(name, value);
        }
    }

    public void addContextScriptProcessor(ContextScriptProcessor csp) {
        contextScriptProcessors.add(csp);
    }
}
