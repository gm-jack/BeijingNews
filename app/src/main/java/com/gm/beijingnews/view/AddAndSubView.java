package com.gm.beijingnews.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gm.beijingnews.R;

/**
 * Created by Administrator on 2016/7/10.
 */
public class AddAndSubView extends LinearLayout implements View.OnClickListener {
    private Button btn_add, btn_sub;
    private TextView tv_number;
    private int value=1;
    private int minValue=1;
    private int maxValue=20;

    public int getValue() {
        return Integer.parseInt(tv_number.getText().toString());
    }

    public void setValue(int value) {
        this.value = value;
        tv_number.setText(value + "");
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public AddAndSubView(Context context) {
        this(context, null);
    }

    public AddAndSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddAndSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.add_sub_view, this);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        tv_number = (TextView) findViewById(R.id.tv_number);
        btn_add.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
        tv_number.setText(getValue() + "");


        TintTypedArray tty = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.addsubview);
        int ttyValue = tty.getInt(R.styleable.addsubview_value, 1);
        if (ttyValue > 0) {
            setValue(ttyValue);
        }
        int ttyMinValue = tty.getInt(R.styleable.addsubview_minValue, 1);
        if (ttyValue > 0) {
            setMinValue(ttyMinValue);
        }
        int ttyMaxValue = tty.getInt(R.styleable.addsubview_maxValue, 1);
        if (ttyValue > 0) {
            setMaxValue(ttyMaxValue);
        }
        Drawable textDrawable = tty.getDrawable(R.styleable.addsubview_textBackground);
        if (textDrawable != null) {
            tv_number.setBackground(textDrawable);
        }
        Drawable btnSubDrawable = tty.getDrawable(R.styleable.addsubview_btnSubBackground);
        if (btnSubDrawable != null) {
            btn_sub.setBackground(btnSubDrawable);
        }
        Drawable btnAddDrawable = tty.getDrawable(R.styleable.addsubview_btnAddBackground);
        if (btnAddDrawable != null) {
            btn_add.setBackground(btnAddDrawable);
        }
        tty.recycle();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addNumber();
                //setValue(value);
                if (mOnNumberAddSubListener != null) {
                    mOnNumberAddSubListener.addNumber(v, value);
                }
                break;
            case R.id.btn_sub:
                subNumber();
                //setValue(value);
                if (mOnNumberAddSubListener != null) {
                    mOnNumberAddSubListener.subNumber(v, value);
                }
                break;
        }
    }

    private void subNumber() {
        if (value > minValue) {
            value--;
            setValue(value);
            //tv_number.setText(value + "");
        }
    }

    private void addNumber() {
        if (value < maxValue) {
            value++;
            setValue(value);
            //tv_number.setText(value + "");
        }
    }

    public interface OnNumberAddSubListener {
        void subNumber(View view, int value);

        void addNumber(View view, int value);
    }

    private OnNumberAddSubListener mOnNumberAddSubListener;

    public void setOnNumberChangeListener(OnNumberAddSubListener onNumberAddSubListener) {
        mOnNumberAddSubListener = onNumberAddSubListener;
    }
}
