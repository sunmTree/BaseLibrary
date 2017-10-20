package com.sunm.base.library.net;

import java.util.HashMap;

public interface IHttpRequest {

    void setHttpHead(HashMap<String, String> head);

    void requestGET(String url, BaseCallback callback);

    void requestPOST(String url, HashMap<String, String> postBody, BaseCallback callback);
}
