//package com.gm.beijingnews.view;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.widget.LinearLayout;
//
//import com.gm.beijingnews.R;
//import com.gm.beijingnews.utils.DpUtil;
//
///**
// * Created by Administrator on 2016/6/30.
// */
//public class MyPagerIndicator extends LinearLayout {
//    private Paint mPaint;
//    //默认tab个数
//    private static final int DEFAULT_ITEM_COUNT = 4;
//    private int mItemCount;
//    private int mColor;
//
//
//    //指示符的宽和高
//    private float mWidth;
//    private float mHeight;
//
//    private int mTop;
//
//    public MyPagerIndicator(Context context) {
//        this(context, null);
//    }
//
//    public MyPagerIndicator(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        //初始化自定义属性
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Indicator);
//        mColor = ta.getColor(R.styleable.Indicator_color, 0x0000FF);
////        mItemCount = ta.getInt(R.styleable.ViewPagerIndicator_item_count, DEFAULT_ITEM_COUNT);
////        if (mItemCount < 0) {
////            mItemCount = DEFAULT_ITEM_COUNT;
////        }
//        ta.recycle();
//
//        //初始化画笔
//        mPaint = new Paint();
//        mPaint.setAntiAlias(true);
//        mPaint.setColor(mColor);
//        //设置绘制直线连接处的圆角效果
//        //mPaint.setPathEffect(new CornerPathEffect(3));
//
//        mHeight = DpUtil.dp2px(context, 5);
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        mItemCount = getChildCount();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mTop = getMeasuredHeight();//tab标签的高度
//        int height = (int) (mTop + mHeight);//标签与指示符的高度即为现在标签的高度
//        int width = getMeasuredWidth();
//        mWidth = width / mItemCount;
//        setMeasuredDimension(width, height);
//    }
//
//    private int mLeft;
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        Rect rect = new Rect(mLeft, mTop, (int) (mLeft + mWidth), (int) (mTop + mHeight));
//        //绘制矩形
//        canvas.drawRect(rect, mPaint);
//        super.onDraw(canvas);
//    }
//
//    /**
//     * @param position 当前位置
//     * @param offset   偏移量
//     */
//    public void scroll(int position, float offset) {
//        mLeft = (int) ((position + offset) * mWidth);
//        postInvalidate();
//    }
//}
