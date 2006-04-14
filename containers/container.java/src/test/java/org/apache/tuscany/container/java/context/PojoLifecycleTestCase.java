/**
 *
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
package org.apache.tuscany.container.java.context;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.tuscany.container.java.mock.MockFactory;
import org.apache.tuscany.container.java.mock.components.ModuleScopeInitOnlyComponent;
import org.apache.tuscany.container.java.mock.components.ModuleScopeInitDestroyComponent;
import org.apache.tuscany.core.context.CompositeContext;
import org.apache.tuscany.core.context.impl.CompositeContextImpl;
import org.apache.tuscany.model.assembly.Scope;

/**
 * Tests <code>@Init</code> method called, <code>@Context</code> set for ModuleContext, <code>@ComponentName</code> set
 *
 * @version $Rev$ $Date$
 */
public class PojoLifecycleTestCase extends TestCase {

    public void testComponentNameSet() throws Exception {
        CompositeContext mc = new CompositeContextImpl();
        mc.setName("mc");
        JavaComponentContext context = MockFactory.createPojoContext("TestServiceInit",
                ModuleScopeInitOnlyComponent.class, Scope.MODULE, mc);
        context.start();
        ModuleScopeInitOnlyComponent instance = (ModuleScopeInitOnlyComponent) context.getInstance(null);
        Assert.assertNotNull(instance);
        Assert.assertEquals("TestServiceInit", instance.getName());
        context.stop();
    }

    public void testModuleContextSet() throws Exception {
        CompositeContext mc = new CompositeContextImpl();
        mc.setName("mc");
        JavaComponentContext context = MockFactory.createPojoContext("TestServiceInit",
                ModuleScopeInitOnlyComponent.class, Scope.MODULE, mc);
        context.start();
        ModuleScopeInitOnlyComponent instance = (ModuleScopeInitOnlyComponent) context.getInstance(null);
        Assert.assertNotNull(instance);
        Assert.assertEquals(mc, instance.getModuleContext());
        context.stop();
    }

    public void testInit() throws Exception {
        CompositeContext mc = new CompositeContextImpl();
        mc.setName("mc");
        JavaComponentContext context = MockFactory.createPojoContext("TestServiceInit",
                ModuleScopeInitOnlyComponent.class, Scope.MODULE, mc);
        context.start();
        ModuleScopeInitOnlyComponent instance = (ModuleScopeInitOnlyComponent) context.getInstance(null);
        Assert.assertNotNull(instance);
        Assert.assertTrue(instance.isInitialized());
        context.stop();
    }


    public void testDestroy() throws Exception {
        CompositeContext mc = new CompositeContextImpl();
        mc.setName("mc");
        JavaComponentContext context = MockFactory.createPojoContext("TestServiceInit",
                ModuleScopeInitDestroyComponent.class, Scope.MODULE, mc);
        context.start();
        ModuleScopeInitDestroyComponent instance = (ModuleScopeInitDestroyComponent) context.getInstance(null);
        Assert.assertNotNull(instance);
        context.destroy();
        Assert.assertTrue(instance.isDestroyed());
        context.stop();
    }
}
