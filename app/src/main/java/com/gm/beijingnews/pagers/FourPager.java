package com.gm.beijingnews.pagers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gm.beijingnews.R;
import com.gm.beijingnews.adapter.PayAdapter;
import com.gm.beijingnews.base.BasePager;
import com.gm.beijingnews.domain.ShopCarData;
import com.gm.beijingnews.utils.CartProvider;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 * 政要界面
 */
public class FourPager extends BasePager {
    private RecyclerView rvPay;
    private CheckBox cbCheckAll;
    private TextView tvPriceAll;
    private Button btnPay;
    private PayAdapter mPayAdapter;
    private CartProvider mCartProvider;
    private List<ShopCarData> allData;

    public FourPager(Context context) {
        super(context);
        mCartProvider = CartProvider.with(context);
    }

    @Override
    public void initData() {
        View view = View.inflate(context, R.layout.pay_pager, null);
        rvPay = (RecyclerView) view.findViewById(R.id.rv_pay);
        cbCheckAll = (CheckBox) view.findViewById(R.id.cb_check_all);
        tvPriceAll = (TextView) view.findViewById(R.id.tv_price_all);
        btnPay = (Button) view.findViewById(R.id.btn_pay);
        getData();
        mPayAdapter = new PayAdapter(context, allData, cbCheckAll, tvPriceAll, tv_pay_edit, btnPay);
        rvPay.setAdapter(mPayAdapter);
        rvPay.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        fl_base.removeAllViews();
        fl_base.addView(view);
    }

    private void getData() {
        allData = mCartProvider.getAllData();
        LogUtil.e(allData.size() + "");
    }
}
