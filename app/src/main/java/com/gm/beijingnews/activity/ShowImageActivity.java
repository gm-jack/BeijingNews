package com.gm.beijingnews.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.domain.GroupPhotoBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class ShowImageActivity extends AppCompatActivity implements View.OnClickListener {

    private int CurrentPosition;
    private ViewPager vp_show_image;
    private ImageView ivShowImageBack;
    private TextView tvShowImageTitle;
    private TextView ibShare;
    private TextView ibLocalNet;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-07-06 20:09:20 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        vp_show_image = (ViewPager) findViewById(R.id.vp_show_image);
        ivShowImageBack = (ImageView) findViewById(R.id.iv_show_image_back);
        tvShowImageTitle = (TextView) findViewById(R.id.tv_show_image_title);
        ibShare = (TextView) findViewById(R.id.ib_share);
        ibLocalNet = (TextView) findViewById(R.id.ib_local_net);

        ivShowImageBack.setOnClickListener(this);
        ibShare.setOnClickListener(this);
        ibLocalNet.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        findViews();
        getData();
        initData();
    }

    private void initData() {
        vp_show_image.setAdapter(new MyPagerAdapter());
        vp_show_image.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    private ArrayList<GroupPhotoBean.DataBean.NewsBean> list;

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        list = (ArrayList<GroupPhotoBean.DataBean.NewsBean>) bundle.getSerializable("photo");
        CurrentPosition = bundle.getInt("position");
        updateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show_image_back:
                finish();
                break;
            case R.id.ib_share:

                break;
            case R.id.ib_local_net:

                break;
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            CurrentPosition = position;
            updateUI();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void updateUI() {
        tvShowImageTitle.setText((CurrentPosition + 1) + "/" + list.size());
    }

    private ProgressDialog dialog;

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            final PhotoView photoView = new PhotoView(ShowImageActivity.this);
            //PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
            DisplayImageOptions options = DisplayImageOptions.createSimple();
            ImageLoader.getInstance().loadImage(list.get(position).getListimage(), options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    dialog = new ProgressDialog(ShowImageActivity.this);
                    dialog.setProgressStyle(R.style.ImageloadingDialogStyle);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    dialog.dismiss();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    dialog.dismiss();
                    photoView.setImageBitmap(bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    dialog.dismiss();
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
