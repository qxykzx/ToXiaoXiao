package com.qx.toxiaoxiao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by QX on 2018/11/25.
 */

public class internetActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        Button openButton = (Button)findViewById(R.id.internet_open);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextInternet = (EditText)findViewById(R.id.internet_webaddr);
                String address = editTextInternet.getText().toString();
                WebView webView = (WebView)findViewById(R.id.internet_webview);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url){
                        view.loadUrl(url);
                        return  true;
                    }
                });
                webView.loadUrl(address);
            }
        });
    }

}
