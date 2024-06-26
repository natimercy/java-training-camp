/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.middleware.rpc.client;

import com.acme.middleware.rpc.codec.MessageDecoder;
import com.acme.middleware.rpc.codec.MessageEncoder;
import com.acme.middleware.rpc.loadbalancer.ServiceInstanceSelector;
import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.discovery.ServiceDiscovery;
import com.acme.middleware.rpc.transport.InvocationResponseHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 客户端引导程序
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class RpcClient implements AutoCloseable {

    private final ServiceDiscovery serviceDiscovery;

    private final ServiceInstanceSelector selector;

    private final Bootstrap bootstrap;

    private final EventLoopGroup group;

    public RpcClient(ServiceDiscovery serviceDiscovery, ServiceInstanceSelector selector) {
        this.serviceDiscovery = serviceDiscovery;
        this.selector = selector;
        this.bootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();
        this.bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("message-encoder", new MessageEncoder());
                        ch.pipeline().addLast("message-decoder", new MessageDecoder());
                        ch.pipeline().addLast("response-handler", new InvocationResponseHandler());
                    }
                });

        serviceDiscovery.initialize((Map) System.getProperties());
    }

    public RpcClient() {
        this(ServiceDiscovery.DEFAULT, ServiceInstanceSelector.DEFAULT);
    }

    public <T> T getService(String serviceName, Class<T> serviceInterfaceClass) {
        ClassLoader classLoader = serviceInterfaceClass.getClassLoader();
        return (T) Proxy.newProxyInstance(classLoader, new Class[]{serviceInterfaceClass},
                new ServiceInvocationHandler(serviceName, this));
    }

    public ChannelFuture connect(ServiceInstance serviceInstance) {
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();
        ChannelFuture channelFuture = bootstrap.connect(host, port);
        return channelFuture.awaitUninterruptibly();
    }

    protected ServiceDiscovery getServiceRegistry() {
        return serviceDiscovery;
    }

    protected ServiceInstanceSelector getSelector() {
        return selector;
    }

    protected Bootstrap getBootstrap() {
        return bootstrap;
    }

    @Override
    public void close() throws Exception {
        group.shutdownGracefully();
    }
}
