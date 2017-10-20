package com.sunm.base.library.net;


import com.sunm.base.library.net.okhttp.OkHttpRequestImpl;

import java.util.HashMap;

/**
 * 今天晚上只是实现大体的框架，因为正则的问题，暂时只支持返回String 和 bitmap格式
 */
public class HttpRequestUtils {

    private IHttpRequest mHttpRequest;

    public HttpRequestUtils() {
        this.mHttpRequest = new OkHttpRequestImpl();
    }

    public void setHttpRequest(IHttpRequest request) {
        this.mHttpRequest = request;
    }

    public void startRequest(String url, BaseCallback callback) {
        mHttpRequest.requestGET(url, callback);
    }

    public void startRequest(String url, HashMap<String, String> httpBody, BaseCallback callback) {
        mHttpRequest.requestPOST(url, httpBody, callback);
    }

}
