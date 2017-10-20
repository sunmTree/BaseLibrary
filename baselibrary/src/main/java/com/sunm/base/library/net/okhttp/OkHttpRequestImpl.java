package com.sunm.base.library.net.okhttp;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.sunm.base.library.net.BaseCallback;
import com.sunm.base.library.net.IHttpRequest;
import com.sunm.base.library.net.StringCallBack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class OkHttpRequestImpl implements IHttpRequest {

    private OkHttpClient mClient;
    private HashMap<String, String> mRequestHead;

    public OkHttpRequestImpl() {
        mClient = new OkHttpClient();
        mClient.setConnectTimeout(10, TimeUnit.SECONDS);
        mClient.setReadTimeout(5, TimeUnit.SECONDS);
        mClient.setWriteTimeout(30, TimeUnit.SECONDS);
    }

    @Override
    public void setHttpHead(HashMap<String, String> head) {
        mRequestHead = head;
    }

    @Override
    public void requestGET(String url, final BaseCallback callback) {
        Request request = getRequest(url, null);
        final Call call = mClient.newCall(request);
        call.enqueue(new OkCallBack(callback));
    }

    @Override
    public void requestPOST(String url, HashMap<String, String> postBody, BaseCallback callback) {
        Request request = getRequest(url, postBody);
        final Call call = mClient.newCall(request);
        call.enqueue(new OkCallBack(callback));
    }

    private class OkCallBack implements Callback {

        private BaseCallback callback;

        public OkCallBack(BaseCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            if (callback instanceof StringCallBack) {
                callback.requestFailed(request.toString(), e);
            } else {
                callback.requestFailed(request, e);
            }
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (callback instanceof StringCallBack) {
                callback.requestSuccess(response.body().string());
            } else {
                callback.requestSuccess(response);
            }
        }
    }

    private Request getRequest(String url, HashMap<String, String> postBody) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (mRequestHead != null && !mRequestHead.isEmpty()) {
            Set<Map.Entry<String, String>> entries = mRequestHead.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (postBody != null && !postBody.isEmpty()) {
            FormEncodingBuilder encodingBuilder = new FormEncodingBuilder();
            Set<Map.Entry<String, String>> entries = postBody.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                encodingBuilder.add(entry.getKey(), entry.getValue());
            }
            builder.post(encodingBuilder.build());
        }
        return builder.build();
    }
}
