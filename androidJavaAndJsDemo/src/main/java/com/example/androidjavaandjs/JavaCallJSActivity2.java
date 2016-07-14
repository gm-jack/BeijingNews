package com.example.androidjavaandjs;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Java调用html里面的javascript代码
 * 
 * 这个案例其实是js->Java->js
 * 
 * @author 杨光福
 * 
 */
public class JavaCallJSActivity2 extends Activity {

	private WebView mWebView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showWebView();
	}

	private void showWebView() {
		// webView与js交互代码
		try {
			mWebView = new WebView(this);
			setContentView(mWebView);

			mWebView.requestFocus();

//			mWebView.setWebChromeClient(new MyWebChromeClient());

			mWebView.setOnKeyListener(new MyOnKeyListener());

			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setDefaultTextEncodingName("utf-8");
			 // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
			 //注意Java调用JS的时候addJavascriptInterface是不必须的
			mWebView.addJavascriptInterface(getHtmlObject(), "Android");
//			mWebView.loadUrl("http://10.0.2.2:8080/JavaCallJS2.html");
			mWebView.loadUrl("http://192.168.129.1:8888/JavaCallJS2.html");
//			mWebView.loadUrl("file:///android_asset/JavaCallJS2.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class MyOnKeyListener implements View.OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			}
			return false;

		}

	}

	class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int progress) {
			JavaCallJSActivity2.this.setTitle("Loading...");
			JavaCallJSActivity2.this.setProgress(progress);

			if (progress >= 80) {
				JavaCallJSActivity2.this.setTitle("JsAndroid Test");
			}
		}

	}

	private Object getHtmlObject() {
		Object insertObj = new Object() {
			
			public void JavacallHtml() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mWebView.loadUrl("javascript: showFromHtml()");
						Toast.makeText(JavaCallJSActivity2.this, "clickBtn",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			public void JavacallHtml2() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mWebView.loadUrl("javascript: showFromHtml2('我是阿福')");
						Toast.makeText(JavaCallJSActivity2.this, "clickBtn2",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		};

		return insertObj;
	}

}