package org.apache.tuscany.core.system.component;

import org.apache.tuscany.spi.ObjectFactory;
import org.apache.tuscany.spi.component.PojoConfiguration;
import org.apache.tuscany.spi.injection.EventInvoker;
import org.apache.tuscany.spi.injection.PojoObjectFactory;

import junit.framework.TestCase;
import org.apache.tuscany.core.injection.MethodEventInvoker;

/**
 * Verifies a system atomic component can be started and initialized
 *
 * @version $$Rev$$ $$Date$$
 */
public class SystemAtomicComponentTestCase extends TestCase {

    EventInvoker<Object> initInvoker;

    public void testContextCreationAndInit() throws Exception {
        ObjectFactory<Foo> factory = new PojoObjectFactory<Foo>(Foo.class.getConstructor((Class[]) null), null);
        PojoConfiguration configuration = new PojoConfiguration();
        configuration.addServiceInterface(Foo.class);
        configuration.setObjectFactory(factory);
        configuration.setInitInvoker(initInvoker);
        SystemAtomicComponentImpl context = new SystemAtomicComponentImpl("foo", configuration);
        Foo foo = (Foo) context.createInstance();
        context.init(foo);
        assertNotNull(foo);
        assertTrue(foo.initialized);
    }

    protected void setUp() throws Exception {
        super.setUp();
        initInvoker = new MethodEventInvoker<Object>(Foo.class.getMethod("init"));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static class Foo {

        private boolean initialized;

        public void init() {
            initialized = true;
        }

    }

}
