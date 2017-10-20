package com.sunm.base.library.net.httpclient;


import android.util.Log;

import com.sunm.base.library.AppConfig;
import com.sunm.base.library.net.BaseCallback;
import com.sunm.base.library.net.IHttpRequest;
import com.sunm.base.library.net.StringCallBack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class HttpConnectRequestImpl implements IHttpRequest{
    private static final boolean DEBUG = AppConfig.DEBUG;
    private static final String TAG = "HttpConnect";

    private HttpURLConnection mConnect;

    @Override
    public void setHttpHead(HashMap<String, String> head) {

    }

    @Override
    public void requestGET(final String url, final BaseCallback callback) {
        new Thread(){
            @Override
            public void run() {
                initConnect(url, "GET");
                try {
                    mConnect.connect();
                    InputStream inputStream = mConnect.getInputStream();
                    if (DEBUG) {
                        Log.d(TAG, "request success");
                    }
                    if (callback instanceof StringCallBack) {
                        byte[] bytes = getByteByInputStream(inputStream);
                        callback.requestSuccess(new String(bytes));
                    } else {
                        callback.requestSuccess(inputStream);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void requestPOST(String url, HashMap<String, String> postBody, BaseCallback callback) {

    }

    private void initConnect(String url, String method) {
        try {
            URL requestUrl = new URL(url);
            mConnect = (HttpURLConnection) requestUrl.openConnection();
            mConnect.setConnectTimeout(10000);
            mConnect.setDoInput(true);
            mConnect.setDoOutput(true);
            mConnect.setRequestMethod(method);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getByteByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bufIs = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bufIs.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufIs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
