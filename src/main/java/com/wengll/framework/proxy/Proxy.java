package com.wengll.framework.proxy;

public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
