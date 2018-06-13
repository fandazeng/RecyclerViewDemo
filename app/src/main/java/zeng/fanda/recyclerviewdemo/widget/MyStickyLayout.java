package zeng.fanda.recyclerviewdemo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.transitionseverywhere.TransitionManager;

import zeng.fanda.recyclerviewdemo.R;
import zeng.fanda.recyclerviewdemo.utils.DpUtil;

/**
 * @author 曾凡达
 * @date 2018/6/13
 */
public class MyStickyLayout extends FrameLayout {

    /**
     * 分别记录上次滑动的坐标
     */
    private float mLastY = 0;

    /**
     * 分别记录上次滑动的坐标(onInterceptTouchEvent)
     */
    private float mLastYIntercept = 0;

    private int mTouchSlop;
    private int mMaximumVelocity, mMinimumVelocity;

    private View mContentView,mHeadView,mBottomView;

    private VelocityTracker mVelocityTracker;

    private FrameLayout.LayoutParams mBottomViewParams,mHeadViewParams,mContentViewParams;
    private int mOriginalRecyclerMargin,mOriginalHeadMargin,mOriginalMiddleMargin,mOriginalContentMargin;

    private ValueAnimator mRecyclerAnimator,mHeadAnimator,mContentAnimator;

    private TopTouchEventListener mTopTouchEventListener;

    private STATUS mStatus = STATUS.COLLAPSED;

    private enum STATUS {
        EXPANDED, COLLAPSED
    }


    public MyStickyLayout(@NonNull Context context) {
        this(context, null);
    }

    public MyStickyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyStickyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = findViewById(R.id.sticky_content_view);
        mHeadView = findViewById(R.id.sticky_head_view);
        mBottomView = findViewById(R.id.sticky_recycler_view);
        mBottomViewParams = (LayoutParams) mBottomView.getLayoutParams();
        mHeadViewParams = (LayoutParams) mHeadView.getLayoutParams();
        mContentViewParams = (LayoutParams) mContentView.getLayoutParams();
        mOriginalRecyclerMargin = mBottomViewParams.topMargin;
        mOriginalHeadMargin = mHeadViewParams.topMargin;
        mOriginalContentMargin = mContentViewParams.topMargin;
        mOriginalMiddleMargin = mOriginalRecyclerMargin + mOriginalHeadMargin;

    }

    public void setTopTouchEventListener(TopTouchEventListener topTouchEventListener) {
        mTopTouchEventListener = topTouchEventListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastYIntercept = y;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mLastYIntercept;
                if (mStatus == STATUS.EXPANDED) {
                    if (mTopTouchEventListener != null) {
                        if (mTopTouchEventListener.isBottomViewTop()) {
                            if (deltaY > 0) {
                                return true;
                            }
                        }
                    }
                } else if (Math.abs(deltaY) > mTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastYIntercept = 0;
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mLastY;
                if (deltaY < 0) {//上滑
                    //底部View处理
                    mBottomViewParams.topMargin += deltaY;
                    mBottomView.setLayoutParams(mBottomViewParams);

                    //头部View处理
                    float moveheight = mOriginalRecyclerMargin - mBottomViewParams.topMargin;
                    float percent = moveheight / mOriginalMiddleMargin;
                    mHeadViewParams.topMargin = mOriginalHeadMargin - (int) (percent * mOriginalHeadMargin);

                    //内容View处理
                    mContentViewParams.topMargin = (int) (percent * mOriginalHeadMargin);
                    mContentView.setLayoutParams(mContentViewParams);
                    Log.d("MyStickyLayout", "mOriginalRecyclerMargin = " + mOriginalRecyclerMargin + " , topMargin = " + mHeadViewParams.topMargin + " , mOriginalMiddleMargin = " + mOriginalMiddleMargin + ", moveheight= " + moveheight + ", percent = " + percent);
                    mHeadView.setLayoutParams(mHeadViewParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                dealContentAnimator();
                dealHeadAnimator();
                dealRecyclerAnimator();
                if (mBottomViewParams.topMargin < mOriginalRecyclerMargin / 2) {
                    mStatus = STATUS.EXPANDED;
                    Log.d("MyStickyLayout", "STATUS=" + mStatus);
                }
            default:
                break;
        }
        mLastY = y;
        return super.onTouchEvent(event);
    }

    private void dealContentAnimator() {
        if (mBottomViewParams.topMargin > mOriginalRecyclerMargin / 2) {
            mContentAnimator = ValueAnimator.ofInt(mContentViewParams.topMargin, mOriginalContentMargin).setDuration(300);
        } else {
            mContentAnimator = ValueAnimator.ofInt(mContentViewParams.topMargin, 0).setDuration(300);
        }
        mContentAnimator.removeAllUpdateListeners();
        mContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentViewParams .topMargin = (int) valueAnimator.getAnimatedValue();
                mContentView.setLayoutParams(mContentViewParams);
            }
        });
        mContentAnimator.start();
    }

    private void dealHeadAnimator() {
        if (mBottomViewParams.topMargin > mOriginalRecyclerMargin / 2) {
            mHeadAnimator = ValueAnimator.ofInt(mHeadViewParams.topMargin, mOriginalHeadMargin).setDuration(300);
        } else {
            mHeadAnimator = ValueAnimator.ofInt(mHeadViewParams.topMargin, 0).setDuration(300);
        }
        mHeadAnimator.removeAllUpdateListeners();
        mHeadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mHeadViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mHeadView.setLayoutParams(mHeadViewParams);
            }
        });
        mHeadAnimator.start();
    }

    private void dealRecyclerAnimator() {
        if (mBottomViewParams.topMargin > mOriginalRecyclerMargin / 2) {
            mRecyclerAnimator = ValueAnimator.ofInt(mBottomViewParams.topMargin, mOriginalRecyclerMargin).setDuration(300);
        } else {
            mRecyclerAnimator = ValueAnimator.ofInt(mBottomViewParams.topMargin, Math.abs(mOriginalHeadMargin)).setDuration(300);
        }
        mRecyclerAnimator.removeAllUpdateListeners();
        mRecyclerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBottomViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mBottomView.setLayoutParams(mBottomViewParams);
            }
        });
        mRecyclerAnimator.start();
    }

    public interface TopTouchEventListener {
        boolean isBottomViewTop();
    }

}
