package com.gm.beijingnews.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gm.beijingnews.R;
import com.gm.beijingnews.domain.ShopCarData;
import com.gm.beijingnews.domain.WaresBean;
import com.gm.beijingnews.utils.CartProvider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2016/7/9.
 */
public class WaresAdapter extends RecyclerView.Adapter<WaresAdapter.ViewHolder> {
    private List<WaresBean.ListBean> list;
    private Context context;
    public static int position;
    private CartProvider mCartProvider;

    public WaresAdapter(Context context, List<WaresBean.ListBean> list) {
        this.context = context;
        this.list = list;
//        mCartProvider = CartProvider.with(context);
        mCartProvider = CartProvider.with(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_wares_pager, null);
        return new ViewHolder(view);
    }

    public void refreshItem(List<WaresBean.ListBean> lists) {
        list.clear();
        list = lists;
        notifyDataSetChanged();
    }

    public void addItem(List<WaresBean.ListBean> lists) {
        list.addAll(lists);
        notifyItemRangeChanged(list.size(), list.size() + lists.size());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        WaresBean.ListBean listBean = list.get(position);
        final String imgUrl = list.get(position).getImgUrl();
//        DisplayImageOptions options=new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();
        holder.ivIcon.setTag(imgUrl);
        showImage(holder, imgUrl);
        holder.tvName.setText(listBean.getName());
        holder.tvPrice.setText("￥ " + listBean.getPrice());
    }

    //显示图片
    private void showImage(final ViewHolder holder, final String imgUrl) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();

        ImageLoader.getInstance().displayImage(imgUrl, holder.ivIcon, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (!imageUri.equals(imgUrl)) {
                    return;
                }
                holder.ivIcon.setImageResource(R.drawable.pic_item_list_default);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (view.getTag().equals(imageUri) && loadedImage != null) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap(loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView tvName;
        private TextView tvPrice;
        private Button btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            btnAdd = (Button) itemView.findViewById(R.id.btn_add);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WaresBean.ListBean listBean = list.get(getLayoutPosition());
                    Toast.makeText(context, getLayoutPosition() + "   " + listBean.getId(), Toast.LENGTH_SHORT).show();
                    mCartProvider.addItem(getShopData(listBean));
                }
            });
        }
    }

    private ShopCarData getShopData(WaresBean.ListBean listBean) {
        ShopCarData shopData = new ShopCarData();
        shopData.setId(listBean.getId());
        shopData.setDescription(listBean.getDescription());
        shopData.setImgUrl(listBean.getImgUrl());
        shopData.setName(listBean.getName());
        shopData.setPrice(listBean.getPrice());
        shopData.setSale(listBean.getSale());
        return shopData;
    }

//    private OnBuyListener mOnBuyListener;
//
//    interface OnBuyListener {
//        void onbuy(View view);
//    }
//
//    public void setOnBuyListener(OnBuyListener onBuyListener) {
//        this.mOnBuyListener = onBuyListener;
//
//    }
}
