package com.acme.rpc.seata;

import com.acme.middleware.rpc.InvocationRequest;
import com.acme.middleware.rpc.client.RequestInterceptor;
import com.acme.middleware.rpc.service.ServiceInstance;
import io.seata.common.util.StringUtils;
import io.seata.core.context.RootContext;
import io.seata.core.model.BranchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @auther natimercy
 * @since 2023-05-17
 * @version 1.0.0
 */
public class TransactionPropagationInterceptor implements RequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionPropagationInterceptor.class);

    @Override
    public boolean supported(InvocationRequest request, ServiceInstance serviceInstance) {
        return true;
    }

    @Override
    public void beforeInvoke(InvocationRequest request, ServiceInstance serviceInstance) {
        String xid = RootContext.getXID();
        BranchType branchType = RootContext.getBranchType();
        boolean bind = false;
        if (xid != null) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put(RootContext.KEY_XID, xid);
            metadata.put(RootContext.KEY_BRANCH_TYPE, branchType.name());
            request.setMetadata(metadata);
        }
    }

    @Override
    public Object afterInvoke(InvocationRequest request, ServiceInstance serviceInstance, Object result, Throwable ex) {
        BranchType previousBranchType = RootContext.getBranchType();
        String unbindXid = RootContext.unbind();
        if (BranchType.TCC == previousBranchType) {
            RootContext.unbindBranchType();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unbind xid [{}] branchType [{}] from RootContext", unbindXid, previousBranchType);
        }
        if (unbindXid != null) {
            RootContext.bind(unbindXid);
            LOGGER.warn("bind xid [{}] back to RootContext", unbindXid);
            if (BranchType.TCC == previousBranchType) {
                RootContext.bindBranchType(BranchType.TCC);
                LOGGER.warn("bind branchType [{}] back to RootContext", previousBranchType);
            }
        }

        return result;
    }

}
