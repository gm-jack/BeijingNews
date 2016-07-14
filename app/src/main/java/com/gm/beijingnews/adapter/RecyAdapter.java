package com.gm.beijingnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.domain.VideoData;
import com.gm.beijingnews.utils.Constact;
import com.gm.beijingnews.utils.SmallBitmapUtil;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Administrator on 2016/7/9.
 */
public class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.ViewHolder> {
    private List<VideoData> list;
    private Context context;
    public static int position;
    private SmallBitmapUtil util;

    public RecyAdapter(Context context, List<VideoData> list) {
        this.context = context;
        this.list = list;
        util = new SmallBitmapUtil();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_homepager, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_home_title.setText(list.get(position).getTitle());
//        LruCache<String, Bitmap> thumb = util.getThumb(list.get(position).getVideoUrl());
//        JCVideoPlayer.releaseAllVideos();
        holder.iv_home_video.setUp(list.get(position).getVideoUrl(), Constact.BASE_URL + "/tomcat.png", list.get(position).getTitle());
    }

    public void addItem(VideoData message) {
        list.add(position, message);
        notifyItemInserted(position);
        position++;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        JCVideoPlayer iv_home_video;
        //        ImageView iv_home_start;
        TextView tv_home_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_home_title = (TextView) itemView.findViewById(R.id.tv_home_title);
            iv_home_video = (JCVideoPlayer) itemView.findViewById(R.id.iv_home_video);
//            iv_home_start = (ImageView) itemView.findViewById(R.id.iv_home_start);
        }
    }
}
