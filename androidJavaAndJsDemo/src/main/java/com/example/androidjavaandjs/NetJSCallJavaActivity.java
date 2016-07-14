package com.example.androidjavaandjs;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NetJSCallJavaActivity extends Activity {

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

			mWebView.setWebChromeClient(new MyWebChromeClient());

			mWebView.setOnKeyListener(new MyOnKeyListener());

			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setDefaultTextEncodingName("utf-8");
			 // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
			mWebView.addJavascriptInterface(getHtmlObject(), "Android");
			mWebView.loadUrl("http://192.168.129.1:8888/netJSCallJava.html");
//			mWebView.loadUrl("file:///android_asset/netJSCallJava.html");
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
			NetJSCallJavaActivity.this.setTitle("Loading...");
			NetJSCallJavaActivity.this.setProgress(progress);
			if (progress >= 80) {
				NetJSCallJavaActivity.this.setTitle("JsAndroid Test");
			}
		}

	}

	private Object getHtmlObject() {
		Object insertObj = new Object() {
			public String HtmlcallJava() {
				
				return "Html call Java";
			}

			public String HtmlcallJava2(final String param) {
				return "Html call Java : " + param;
			}


		};
		return insertObj;
	}

}
