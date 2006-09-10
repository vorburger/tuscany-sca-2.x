/*
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
package org.apache.tuscany.osgi.binding;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;

import org.apache.tuscany.spi.component.CompositeComponent;
import org.apache.tuscany.spi.extension.ServiceExtension;
import org.apache.tuscany.spi.wire.WireService;

import org.apache.tuscany.osgi.OSGiHost;
import org.osgi.framework.ServiceFactory;

/**
 * @version $Rev$ $Date$
 */
public class OSGiService extends ServiceExtension {
    private final String osgiServiceName;
    //private final Class<? extends Object> service;
    private OSGiHost host;

    /**
     * Creates a new service instance
     *
     * @param name            the name of the SCA service
     * @param parent          the containing composite
     * @param wireService     the wire service to use for creating proxies
     * @param osgiServiceName the name to publish the service as to the OSGi hose
     * @param service         the service type
     * @param host            the OSGi host
     */
    public OSGiService(String name,
                       CompositeComponent parent,
                       WireService wireService,
                       String osgiServiceName,
                       Class<?> service,
                       OSGiHost host) {
        super(name, service, parent, wireService);
        this.osgiServiceName = osgiServiceName;
        //this.service = service;
        this.host = host;
    }

    public void start() {
        super.start();
        Properties properties = new Properties();
        Object instance = getServiceInstance();
        if (instance instanceof ServiceFactory) {
            host.registerService(osgiServiceName, instance, properties);
        } else {
            ServiceFactoryProxyHandler factoryProxy = new ServiceFactoryProxyHandler(instance);
            Class<?> interfaces[] = instance.getClass().getInterfaces();
            Class<?> proxyInterfaces[] = new Class<?>[interfaces.length + 1];
            int i;
            for (i = 0; i < interfaces.length; i++) {
                proxyInterfaces[i] = interfaces[i];
            }
            proxyInterfaces[i] = ServiceFactory.class;
            ClassLoader classLoader = instance.getClass().getClassLoader();
            // TODO It seems we need a mixin capability for the service. We could add that to WireService
            Object targetProxy = Proxy.newProxyInstance(classLoader, proxyInterfaces, factoryProxy);
            host.registerService(osgiServiceName, targetProxy, properties);
        }
        //Register the service with OSGi
    }

    public void stop() {
        super.stop();
        //Unregister the service with OSGi
    }

    private class ServiceFactoryProxyHandler implements InvocationHandler {

        private Object instance;

        public ServiceFactoryProxyHandler(Object instance) {
            this.instance = instance;
        }

        public Object invoke(Object object, Method method, Object[] parms) throws Throwable {
            if (method.getName().equals("getService")) {
                return instance;
            } else if (method.getName().equals("ungetService")) {
                return null;
            }
            return method.invoke(instance, parms);
        }

    }
}
