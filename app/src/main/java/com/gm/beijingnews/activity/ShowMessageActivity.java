package com.gm.beijingnews.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.utils.WebsiteUtil;

import org.xutils.common.util.LogUtil;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShowMessageActivity extends Activity implements View.OnClickListener {

    private String messageUrl;
    private ImageView iv_back;
    private ImageView iv_textsize;
    private ImageView iv_share;
    private TextView tv_base;
    private WebView wv_showmessage;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_message);
        getData();
        initView();
        updateView();
        initData();
    }

    private void updateView() {
        iv_back.setVisibility(View.VISIBLE);
        iv_textsize.setVisibility(View.VISIBLE);
        iv_share.setVisibility(View.VISIBLE);
    }

    private void initData() {
        wv_showmessage.loadUrl(messageUrl);
        wv_showmessage.canGoBack();
        wv_showmessage.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
        wv_showmessage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(messageUrl);
                return true;
            }
        });
        settings = wv_showmessage.getSettings();
        settings.setJavaScriptEnabled(true);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_textsize = (ImageView) findViewById(R.id.iv_textsize);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        tv_base = (TextView) findViewById(R.id.tv_base);
        wv_showmessage = (WebView) findViewById(R.id.wv_showmessage);

        iv_back.setOnClickListener(this);
        iv_textsize.setOnClickListener(this);
        iv_share.setOnClickListener(this);
    }

    private void getData() {
        messageUrl = getIntent().getStringExtra("messageUrl");
        if (messageUrl.contains("10.0.2.2:8080")) {
            messageUrl = WebsiteUtil.WebsiteChange(messageUrl);
        }
        LogUtil.e(messageUrl);
    }

    private AlertDialog.Builder dialog;
    private int checkId = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                showShare();
                break;
            case R.id.iv_textsize:
                dialog = new AlertDialog.Builder(this);
                dialog.setTitle("设置字体");
                String[] items = new String[]{"超大字体", "大字体", "中等字体", "小字体", "超小字体"};
                dialog.setSingleChoiceItems(items, checkId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkId = which;
                        LogUtil.e("" + which);
                    }
                });
                dialog.setPositiveButton("确认更改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeTextSize(checkId);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("取消更改", null);
                dialog.show();
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("哈哈哈");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("啊啊岁的萨德v");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    /**
     * 设置webview中字体大小
     *
     * @param which
     */
    private void changeTextSize(int which) {
        switch (which) {
            case 0:
                settings.setTextZoom(200);
                break;
            case 1:
                settings.setTextZoom(150);
                break;
            case 2:
                settings.setTextZoom(100);
                break;
            case 3:
                settings.setTextZoom(75);
                break;
            case 4:
                settings.setTextZoom(60);
                break;
        }
    }
}
