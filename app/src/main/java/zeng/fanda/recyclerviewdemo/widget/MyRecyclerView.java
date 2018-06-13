package zeng.fanda.recyclerviewdemo.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author 曾凡达
 * @date 2018/6/12
 */
public class MyRecyclerView extends RecyclerView {

    private boolean mIsInterceptTouchEvent;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setInterceptTouchEvent(boolean interceptTouchEvent) {
        mIsInterceptTouchEvent = interceptTouchEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (mIsInterceptTouchEvent) {
            return mIsInterceptTouchEvent;
        } else {
            return super.onInterceptTouchEvent(e);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mIsInterceptTouchEvent) {
            return mIsInterceptTouchEvent;
        } else {
            return super.onTouchEvent(e);
        }
    }
}
