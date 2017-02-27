package com.example.androidjavascript0227;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button js;
    private Button withjs;
    private WebView mWebView;
    final public static int REQUEST_CODE = 123;
    String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/js.html");
        mWebView.addJavascriptInterface(MainActivity.this,"android");

        js = (Button) findViewById(R.id.buttonjs);
        withjs = (Button) findViewById(R.id.buttonjswith);

       js.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljs()");
            }
        });

        withjs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:javacalljswith("+"'http://liangruijun.blog.51cto.com/3061169/647456/'"+")");
                //contentWebView.loadUrl("javascript:javacalljswith(" + "'http://blog.csdn.net/Leejizhou'" + ")");
        }
        });

    }

    @JavascriptInterface
    public void startFunction(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "调用java代码", Toast.LENGTH_SHORT).show();

            }

        });

    }

    @JavascriptInterface
    public void add(final String s1,final String s2){
        int i = new Integer(s1);
        int j = new Integer(s2);
        final int total = i+j;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "两数之和："+total, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @JavascriptInterface
    public void call(final String s){
        phoneNumber = s;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CallPhone(phoneNumber);
            }

        });
    }

    /*@JavascriptInterface
    public void send(final String p){
        final String msg = "你好";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.setWebViewClient(new WebViewClient(){

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if(PhoneNumberUtils.isGlobalPhoneNumber(p)){
                            Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+p));
                            intent.putExtra("sms_body",msg);
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

        });
    }*/

    private void CallPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNumber));
        if(Build.VERSION.SDK_INT >= 23){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE);
                return;
            }else {
                startActivity(intent);
            }
        }else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CallPhone(phoneNumber);
            }else {
                Toast.makeText(MainActivity.this, "权限被拒", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
