package com.gm.beijingnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list;
    private LayoutInflater inflate;
    private int layoutID;

    public MyBaseAdapter(Context context, List<T> list, int layoutID) {
        this.context = context;
        inflate = LayoutInflater.from(context);
        this.list = list;
        this.layoutID = layoutID;
    }

    @Override
    public int getCount() {
        return (null == list) ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PubViewHolder viewholder = new PubViewHolder(null, inflate, layoutID);
        convert(viewholder, position);
        return viewholder.getConvertView();
    }

    public abstract void convert(PubViewHolder viewholder, int position);

    /**
     * 刷新
     */
    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    /**
     * 刷新
     *
     * @param newList
     */
    public void refreshAdapter(List<T> newList) {
        list.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * 清空集合
     */
    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    /**
     * 移除指定位置的对象
     */
    public void delList(int index) {
        list.remove(index);
        notifyDataSetChanged();
    }

    /**
     * 移除指定对象
     */
    public void delList(Object object) {
        list.remove(object);
        notifyDataSetChanged();
    }
}
