package com.gm.beijingnews.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gm.beijingnews.R;
import com.gm.beijingnews.domain.ShopCarData;
import com.gm.beijingnews.payutils.PayResult;
import com.gm.beijingnews.payutils.SignUtils;
import com.gm.beijingnews.utils.CartProvider;
import com.gm.beijingnews.view.AddAndSubView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Administrator on 2016/7/11.
 */
public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {

    private Button btnPay;
    private TextView tvpatedit;
    private TextView tvPriceAll;
    private CheckBox cbCheckAll;
    private List<ShopCarData> allData;
    private Context context;
    private CartProvider mCartProvider;
    private boolean isEdit = false;

    public PayAdapter(Context context, final List<ShopCarData> allData, CheckBox cbCheckAll, TextView priceAll, final TextView tvpatedit, Button btnPay) {
        this.context = context;
        this.allData = allData;
        this.cbCheckAll = cbCheckAll;
        this.tvPriceAll = priceAll;
        this.tvpatedit = tvpatedit;
        this.btnPay = btnPay;
        mCartProvider = CartProvider.with(context);
        showAllPrice();

    }

    /**
     * 显示所有商品的价格总和
     */
    private void showAllPrice() {
        tvPriceAll.setText("合计: ￥" + getAllPrice());
    }

    /**
     * 获取所有选中商品的价格总和
     *
     * @return
     */
    private float getAllPrice() {
        int count = 0;
        for (int i = 0; i < allData.size(); i++) {
            ShopCarData shopCarData = allData.get(i);
            if (shopCarData.isChecked()) {
                count += shopCarData.getCount() * shopCarData.getPrice();
            }
        }
        return count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_pay_pager, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopCarData data = allData.get(position);
        holder.aasv_add_sub.setValue(data.getCount());
        holder.cbPayPager.setChecked(data.isChecked());
        holder.tvPayPagerName.setText(data.getName());
        holder.ivPayPager.setTag(data.getImgUrl());
        holder.tvPayPagerPrice.setText(data.getPrice() + "");
        showImage(holder, data.getImgUrl());
    }

    @Override
    public int getItemCount() {
        return allData.size();
    }

    //显示图片
    private void showImage(final ViewHolder holder, final String imgUrl) {
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).build();

        ImageLoader.getInstance().displayImage(imgUrl, holder.ivPayPager, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (!imageUri.equals(imgUrl)) {
                    return;
                }
                holder.ivPayPager.setImageResource(R.drawable.pic_item_list_default);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbPayPager;
        private ImageView ivPayPager;
        private TextView tvPayPagerName;
        private TextView tvPayPagerPrice;
        private AddAndSubView aasv_add_sub;

        public ViewHolder(View itemView) {
            super(itemView);
            cbPayPager = (CheckBox) itemView.findViewById(R.id.cb_pay_pager);
            ivPayPager = (ImageView) itemView.findViewById(R.id.iv_pay_pager);
            tvPayPagerName = (TextView) itemView.findViewById(R.id.tv_pay_pager_name);
            tvPayPagerPrice = (TextView) itemView.findViewById(R.id.tv_pay_pager_price);
            aasv_add_sub = (AddAndSubView) itemView.findViewById(R.id.aasv_add_sub);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //切换checkbox状态
                    changeItemChecked(getLayoutPosition());
                    //重新计算显示价格
                    showAllPrice();
                    // 改变全选状态
                    changeCheckAll();
                }
            });
            aasv_add_sub.setOnNumberChangeListener(new AddAndSubView.OnNumberAddSubListener() {
                @Override
                public void subNumber(View view, int value) {
                    ShopCarData shopCarData = allData.get(getLayoutPosition());
                    shopCarData.setCount(value);
                    mCartProvider.updateItem(shopCarData);
                    //重新计算显示价格
                    showAllPrice();
                }

                @Override
                public void addNumber(View view, int value) {
                    ShopCarData shopCarData = allData.get(getLayoutPosition());
                    shopCarData.setCount(value);
                    mCartProvider.updateItem(shopCarData);
                    //重新计算显示价格
                    showAllPrice();
                }
            });
            cbCheckAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < allData.size(); i++) {
                        cbPayPager.setChecked(cbCheckAll.isChecked());
                        checkAllChange();
                    }
                }
            });
            tvpatedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvpatedit.getText().toString().trim().equals("编辑")) {
                        isEdit = true;
                        tvPriceAll.setVisibility(View.INVISIBLE);
                        tvpatedit.setText("完成");
                        btnPay.setText("删除");
                        cbCheckAll.setChecked(false);
                        checkAllChange();
                        // 改变全选状态
                        changeCheckAll();
                    } else if ("完成".equals(tvpatedit.getText().toString().trim())) {
                        isEdit = false;
                        tvPriceAll.setVisibility(View.VISIBLE);
                        showAllPrice();
                        tvpatedit.setText("编辑");
                        btnPay.setText("立即支付");
                        cbCheckAll.setChecked(true);
                        checkAllChange();
                        // 改变全选状态
                        changeCheckAll();
                    }
                }
            });
            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEdit) {
                        //编辑状态
                        if (allData != null && allData.size() > 0) {
//                            for (int i = 0; i < allData.size(); ) {
//                                ShopCarData shopCarData = allData.get(i);
//                                if (shopCarData.isChecked()) {
//                                    mCartProvider.delItem(shopCarData);
//                                    allData.remove(shopCarData);
//                                    notifyItemRemoved(i);
//                                }
//                            }

                            deleteSelectItem();
                        }
                    } else {
                        //支付状态(支付成功：清空购物车，支付失败：无改变)
                        pay(v);
                    }
                }
            });
        }
    }

    /**
     * 删除选中的item
     */
    private void deleteSelectItem() {
        for (Iterator<ShopCarData> iterator = allData.iterator(); iterator.hasNext(); ) {
            ShopCarData shopCarData = iterator.next();
            if (shopCarData.isChecked()) {
                mCartProvider.delItem(shopCarData);
                allData.remove(shopCarData);
                notifyItemRemoved(allData.indexOf(shopCarData));
            }
        }
    }

    /**
     * 编辑状态的checkbox
     */
    private void checkAllChange() {
        if (allData != null && allData.size() > 0) {
            for (int i = 0; i < allData.size(); i++) {
//                if (isEdit)
//                    allData.get(i).setIsChecked(cbCheckAll.isChecked());
//                else
                allData.get(i).setIsChecked(cbCheckAll.isChecked());
                notifyItemChanged(i);
            }
        }
    }

    private void changeItemChecked(int layoutPosition) {
        ShopCarData carData = allData.get(layoutPosition);
        carData.setIsChecked(!carData.isChecked());
        notifyItemChanged(layoutPosition);
    }

    private void changeCheckAll() {
        int num = 0;
        if (allData != null && allData.size() > 0) {
            for (int i = 0; i < allData.size(); i++) {
                if (!allData.get(i).isChecked()) {
                    cbCheckAll.setChecked(false);
                } else {
                    num += 1;
                }
                if (num == allData.size()) {
                    cbCheckAll.setChecked(true);
                }
            }
        }
    }

    // 商户PID
    public static final String PARTNER = "2088911876712776";
    // 商户收款账号
    public static final String SELLER = "chenlei@atguigu.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKWzIMf4juEKLcxW\n" +
            "CghiwjwMi84qr8bK6Z3QigP4gGnxvwenmLpB+TPGATXjyOrW3eUE3pjHMueum5lc\n" +
            "WzZCcX4FCUTjxhjY/6TiYSwZmYd0lL78JlrA6hfdE+/bvNth/7tb4oPujjkJxEL8\n" +
            "Jz9Xun1JuVfKsFsKzrF5/hI8GjhvAgMBAAECgYEAkdeXC8s/Xt2Jr+cuz3C1Swsz\n" +
            "5lN2AT/J3aiWQaZwyE8J95/Q8mdtAt/NYvRJfEGAbroZflkd+gOaWqKKebiDkKAf\n" +
            "txHsiiuiXsNF9oZI7XPfQW2Qs+5fNGI6w+aBe2vrJaTh69ln+RAEJAHVGJir96pd\n" +
            "cMyCdP9DKWbv/AcKC9kCQQDQ5enHX43dHBCQjPheW4/oZ71flSajdZ417NwC8Iaj\n" +
            "JJS2yB28iNA58yz1yMH6Nh3ytrVzPHGi1BsqsOZIo/ZDAkEAyw+0D1dTDOkzW9Vf\n" +
            "5vfY0ubf+fX/k7y9tWA9MMa/kCtxD+AqEBRob0HX7GJE3rXr94VP0PlsfDYSYkW7\n" +
            "0bSwZQJADzEJJUSfrnrCAaEe1s3V01LoBLF2E+ET1lwZS+VuBSM95WT8NJ/GdNBo\n" +
            "bp9g6+9Pkj0TSQQWR38hUqN2u8WgvQJAPQRp+dYmqZGFkknKuI+vQqiHkwfdlZo4\n" +
            "c0EJAhgmxf6Xycp1OUG4o/3oESryQO3viwAvHL9mXuism4USzA/8lQJAVN/1dqPt\n" +
            "7mOP0hPJAlPLsLuzcwIEM7UTpRhqGlnw5CPk/wzf9z8ypqOlxQhF5luLDs2XTWAe\n" +
            "RlMqRi63WTVl+w==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClsyDH+I7hCi3MVgoIYsI8DIvO\n" +
            "Kq/Gyumd0IoD+IBp8b8Hp5i6QfkzxgE148jq1t3lBN6YxzLnrpuZXFs2QnF+BQlE\n" +
            "48YY2P+k4mEsGZmHdJS+/CZawOoX3RPv27zbYf+7W+KD7o45CcRC/Cc/V7p9SblX\n" +
            "yrBbCs6xef4SPBo4bwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        deleteSelectItem();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
}
