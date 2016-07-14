package com.gm.beijingnews.domain;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ShopCarData extends WaresBean.ListBean {
    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {

        return super.toString()+"ShopCarData{" +
                "count=" + count +
                ", isChecked=" + isChecked +
                "} " + super.toString();
    }
}
