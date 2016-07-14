package com.gm.beijingnews.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PubViewHolder {
	private SparseArray<View> views;// 键值对，用来保存listview或gridview中每一行或者每一列的数据，效率高于hashmap;
	private View convertView, childView;

	public PubViewHolder(ViewGroup parent, LayoutInflater inflate, int layoutID) {
		this.views = new SparseArray<>();
		this.convertView = inflate.inflate(layoutID, parent);
		this.convertView.setTag(this);
	}

	 /**
     * 设置保存TAG
     */
    public PubViewHolder setTag(int viewID, int position) {
        childView = getView(viewID);
        childView.setTag(position);
        return this;
    }


    /**
     * 设置点击监听
     */
    public PubViewHolder setOnClickListener(int viewID, View.OnClickListener onClickListener) {
        getView(viewID).setOnClickListener(onClickListener);
        return this;
    }
	/**
	 * 提供静态的方法获取MyViewHolder
	 * 
	 * @param parent
	 * @param inflate
	 * @param layoutID
	 * @return
	 */
	public static PubViewHolder getViewHolder(ViewGroup parent,
			View convertView, LayoutInflater inflate, int layoutID) {
		if (convertView == null) {
			return new PubViewHolder(parent, inflate, layoutID);
		}
		return (PubViewHolder) convertView.getTag();
	}

	/**
	 * 根据ID获取控件的view
	 * 
	 * @param viewID
	 * @return
	 */
	public <T extends View> T getView(int viewID) {
		View view = convertView.findViewById(viewID);
		if (view == null) {
			view = convertView.findViewById(viewID);
			views.put(viewID, view);
		}
		return (T) view;

	}

	public View getConvertView() {
		return convertView;
	}
}
