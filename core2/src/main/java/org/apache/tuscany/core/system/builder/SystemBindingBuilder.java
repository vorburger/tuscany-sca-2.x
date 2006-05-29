package org.apache.tuscany.core.system.builder;

import org.apache.tuscany.core.context.AutowireContext;
import org.apache.tuscany.core.system.context.SystemReferenceContextImpl;
import org.apache.tuscany.core.system.context.SystemServiceContextImpl;
import org.apache.tuscany.core.system.model.SystemBinding;
import org.apache.tuscany.core.system.wire.SystemInboundAutowire;
import org.apache.tuscany.core.system.wire.SystemInboundWire;
import org.apache.tuscany.spi.QualifiedName;
import org.apache.tuscany.spi.builder.BindingBuilder;
import org.apache.tuscany.spi.context.ComponentContext;
import org.apache.tuscany.spi.context.CompositeContext;
import org.apache.tuscany.spi.context.Context;
import org.apache.tuscany.spi.deployer.DeploymentContext;
import org.apache.tuscany.spi.model.BoundReference;
import org.apache.tuscany.spi.model.BoundService;
import org.apache.tuscany.spi.wire.InboundWire;

/**
 * @version $$Rev$$ $$Date$$
 */
public class SystemBindingBuilder implements BindingBuilder<SystemBinding> {

    public Context build(CompositeContext parent, BoundService<SystemBinding> boundService, DeploymentContext deploymentContext) {
        Class<?> interfaze = boundService.getServiceContract().getInterfaceClass();
        QualifiedName targetName = new QualifiedName(boundService.getTarget().getPath());
        ComponentContext target = (ComponentContext) parent.getContext(targetName.getPartName());
        SystemInboundWire<?> wire = new SystemInboundWire(targetName.getPortName(), interfaze, target);
        return new SystemServiceContextImpl(boundService.getName(), wire, parent);
    }

    public Context build(CompositeContext parent, BoundReference<SystemBinding> boundReference, DeploymentContext deploymentContext) {
        assert(parent.getParent() instanceof AutowireContext):"Grandparent not an instance of " + AutowireContext.class.getName();
        AutowireContext autowireContext = (AutowireContext) parent.getParent();
        Class<?> interfaze = boundReference.getServiceContract().getInterfaceClass();
        SystemReferenceContextImpl ctx = new SystemReferenceContextImpl(boundReference.getName(), interfaze, parent);
        InboundWire<?> wire = new SystemInboundAutowire(interfaze, autowireContext);
        ctx.setInboundWire(wire);
        return ctx;
    }
}
