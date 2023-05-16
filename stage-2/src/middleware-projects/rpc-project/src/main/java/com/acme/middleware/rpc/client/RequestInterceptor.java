package com.acme.middleware.rpc.client;

import com.acme.middleware.rpc.InvocationRequest;
import com.acme.middleware.rpc.service.ServiceInstance;

/**
 * RequestInterceptor
 *
 * @author natimercy
 * @since 2023-05-16
 * @version 1.0.0
 */
public interface RequestInterceptor {

    boolean supported(InvocationRequest request, ServiceInstance serviceInstance);

    void beforeInvoke(InvocationRequest request, ServiceInstance serviceInstance);

    Object afterInvoke(InvocationRequest request, ServiceInstance serviceInstance, Object result, Throwable ex);

}
