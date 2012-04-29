package com.ezmeal.lazylist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class LinearLayoutForListView extends LinearLayout {

	private LazyAdapter adapter;
    
    public void bindLinearLayout() {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);

 /*           if (i == count - 1) {
                LinearLayout ly = (LinearLayout) v;
                ly.removeViewAt(2);
            }*/
            addView(v, i);
        }
        Log.v("countTAG", "" + count);
    }

    public LinearLayoutForListView(Context context) {
        super(context);

    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

    }

    /**
     * 获取Adapter
     * 
     * @return adapter
     */
    public LazyAdapter getAdpater() {
        return adapter;
    }

    /**
     * 设置数据
     * 
     * @param adpater
     */
    public void setAdapter(LazyAdapter adpater) {
        this.adapter = adpater;
        removeAllViews();
        bindLinearLayout();
    }

    /**
     * 获取点击事件
     * 
     * @return
     */
    /**
     * 设置点击事件
     * 
     * @param onClickListener
     */
}
