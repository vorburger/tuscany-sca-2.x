package org.apache.tuscany.spi.policy;

import org.apache.tuscany.spi.model.Service;
import org.apache.tuscany.spi.builder.BuilderException;
import org.apache.tuscany.spi.wire.InboundWire;

/**
 * Implementations contribute {@link org.apache.tuscany.spi.wire.Interceptor}s or {@link
 * org.apache.tuscany.spi.wire.MessageHandler}s that handle target-side policy on a wire.
 *
 * @version $$Rev$$ $$Date$$
 */
public interface TargetPolicyBuilder{

    public void build(Service service, InboundWire<?> wire) throws BuilderException;

}
