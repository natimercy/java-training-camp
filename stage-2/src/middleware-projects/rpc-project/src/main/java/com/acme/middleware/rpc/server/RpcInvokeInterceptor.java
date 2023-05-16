package com.acme.middleware.rpc.server;

import com.acme.middleware.rpc.InvocationRequest;
import com.acme.middleware.rpc.InvocationResponse;

/**
 * RpcInvokeInterceptor
 *
 * @auther natimercy
 * @since 2023-05-17
 * @version 1.0.0
 */
public interface RpcInvokeInterceptor {

    default void beforeInvoke(Object service, InvocationRequest request) {
    }

    default void afterInvoke(Object service, InvocationRequest request, InvocationResponse response) {
    }

}
