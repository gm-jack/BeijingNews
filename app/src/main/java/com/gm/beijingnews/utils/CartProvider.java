package com.gm.beijingnews.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.gm.beijingnews.domain.ShopCarData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CartProvider {
    public static final String CART_JSON = "cart_json";
    public static final String SP_CART_JSON = "sp_cart_json";
    private static CartProvider cartProvider;
    private static Context mContext;
    private  SparseArray<ShopCarData> shopData;
//    private List<ShopCarData> dataFromLocal;

//    public CartProvider(Context conetext) {
//        mContext = conetext;
//        shopData = new SparseArray<>(10);
//        list2Sparse();
//    }
    private CartProvider() {
        shopData = new SparseArray<>(10);
        list2Sparse();
    }

    public static CartProvider with(Context conetext) {
        mContext = conetext;
        if(cartProvider==null){
            synchronized (CartProvider.class) {
                if (cartProvider == null) {
                    cartProvider=new CartProvider();
                }
            }
        }
        return cartProvider;
    }

    /**
     * list转换为sparse
     */
    private void list2Sparse() {
        List<ShopCarData> list = getAllData();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                shopData.put(list.get(i).getId(), list.get(i));
            }
        }
    }

    public List<ShopCarData> sparse2List() {
        List<ShopCarData> list = new ArrayList<>();
        for (int i = 0; i < shopData.size(); i++) {
            list.add(shopData.valueAt(i));
        }
        return list;
    }

    /**
     * 获取所有保存数据
     */
    public List<ShopCarData> getAllData() {
        return getDataFromLocal();
    }

    /**
     * 获取保存数据
     *
     * @return
     */
    public List<ShopCarData> getDataFromLocal() {
        List<ShopCarData> jsonList = new ArrayList<>();
        String jsonData = DataConvert.getData(mContext, CART_JSON, SP_CART_JSON);
        if (!TextUtils.isEmpty(jsonData)) {
            jsonList = new Gson().fromJson(jsonData, new TypeToken<List<ShopCarData>>() {
            }.getType());
        }
        return jsonList;
    }

    /**
     * 删除数据
     */
    public void delItem(ShopCarData data) {
        shopData.delete(data.getId());
        commit();
    }



    /**
     * 添加数据
     */
    public void addItem(ShopCarData data) {
        //LogUtil.e("" + data.getId());
        ShopCarData data1 = shopData.get(data.getId());
        if (data1 != null) {
            data1.setCount(data1.getCount() + 1);
        } else {
            data1 = data;
            data1.setCount(1);
        }
//        LogUtil.e(data1.toString());
        shopData.put(data.getId(), data1);
        commit();
    }

    /**
     * 修改数据
     */
    public void updateItem(ShopCarData data) {
        shopData.put(data.getId(), data);
        commit();
    }
    private void commit() {
        List<ShopCarData> shopList = sparse2List();
        if(shopList!=null&&shopList.size()>0){
            String toJson = new Gson().toJson(shopList);
            DataConvert.saveData(mContext,CART_JSON,toJson,SP_CART_JSON);
        }
    }
}
