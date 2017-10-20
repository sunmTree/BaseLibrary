package com.sunm.base;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sunm.base.library.AppConfig;
import com.sunm.base.library.net.HttpRequestUtils;
import com.sunm.base.library.net.StringCallBack;
import com.sunm.base.library.net.httpclient.HttpConnectRequestImpl;

import java.lang.ref.SoftReference;

public class MainActivity extends AppCompatActivity {
    private static final boolean DEBUG = AppConfig.DEBUG;
    private static final String TAG = "MainActivity";

    private TextView mTextView;
    private Handler mHandler;

    private static final int REQUEST_SUCCESS = 0x00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new MyHandler(this);
        mTextView = (TextView) findViewById(R.id.main_text);
        requestData();
    }

    private void requestData() {
        String url = "https://www.baidu.com/";
        HttpRequestUtils requestUtils = new HttpRequestUtils();
        requestUtils.startRequest(url, new StringCallBack() {
            @Override
            public void requestSuccess(String s) {
                if (DEBUG) {
                    Log.d(TAG, Thread.currentThread().getId() + " success result " + s);
                }
                Message message = mHandler.obtainMessage(REQUEST_SUCCESS);
                message.obj = s;
                mHandler.sendMessage(message);
            }

            @Override
            public void requestFailed(String s, Exception e) {
                if (DEBUG) {
                    Log.d(TAG, Thread.currentThread().getName() + " failed result " + s);
                }
            }
        });
    }

    class MyHandler extends Handler {
        private SoftReference<MainActivity> reference;

        public MyHandler(MainActivity activity) {
            this.reference = new SoftReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (reference == null) {
                return;
            }
            MainActivity mainActivity = reference.get();
            switch (msg.what) {
                case REQUEST_SUCCESS:
                    mainActivity.mTextView.setText((String)msg.obj);
                    break;
            }
        }
    }

}
