package com.qfq.tainzhi.videoplayer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by muqing on 2019/6/17.
 * Email: qfq61@qq.com
 */
public class SquareLinearLayout extends LinearLayout {
    public SquareLinearLayout(Context context) {
        super(context);
    }
    
    public SquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public SquareLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
