package zeng.fanda.recyclerviewdemo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import zeng.fanda.recyclerviewdemo.R;

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
    private static final int ANIMATION_TIME = 300;
    private static final int VELOCITY_MAX = 1000;

    private View mContentView, mHeadView, mBottomView;

    private VelocityTracker mVelocityTracker;

    private FrameLayout.LayoutParams mBottomViewParams, mHeadViewParams, mContentViewParams;
    private int mOriginalBottomMargin, mOriginalHeadMargin, mOriginalMiddleMargin, mOriginalContentMargin;

    private ValueAnimator mBottomAnimator, mHeadAnimator, mContentAnimator;

    private TopTouchEventListener mTopTouchEventListener;

    private STATUS mStatus = STATUS.COLLAPSED;
    private DIRECTION mDirection = DIRECTION.UP;

    private enum STATUS {
        EXPANDED, COLLAPSED
    }

    enum DIRECTION {
        UP, DOWN
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
        mOriginalBottomMargin = mBottomViewParams.topMargin;
        mOriginalHeadMargin = mHeadViewParams.topMargin;
        mOriginalContentMargin = mContentViewParams.topMargin;
        mOriginalMiddleMargin = mOriginalBottomMargin + mOriginalHeadMargin;

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
                if (mStatus == STATUS.EXPANDED) {//头部View是展开的状态
                    if (mTopTouchEventListener != null) {
                        if (mTopTouchEventListener.isBottomViewTop()) {//内容View滑到了顶部
                            if (deltaY > 0 && Math.abs(deltaY) > mTouchSlop) {//下滑，父布局拦截
                                return true;
                            }
                        }
                    }
                } else if (Math.abs(deltaY) > mTouchSlop) {//头部View不是展开的状态
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
        mVelocityTracker.addMovement(event);
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = y - mLastY;
                if (deltaY < 0 ) {
                    //上滑
                    mDirection = DIRECTION.UP;
                    //底部View处理
                    mBottomViewParams.topMargin += deltaY;
                    mBottomView.setLayoutParams(mBottomViewParams);

                    //头部View处理
                    float moveheight = mOriginalBottomMargin - mBottomViewParams.topMargin;//底部View移动的高度
                    float percent = moveheight / mOriginalMiddleMargin;//底部View移动的百分比
                    mHeadViewParams.topMargin = mOriginalHeadMargin - (int) (percent * mOriginalHeadMargin);//头部View移动的高度
                    mHeadView.setLayoutParams(mHeadViewParams);

                    //内容View处理，移动的高度等于头部View的高度
                    mContentViewParams.topMargin = (int) (percent * mOriginalHeadMargin);
                    mContentView.setLayoutParams(mContentViewParams);


                    Log.d("MyStickyLayout", "mOriginalBottomMargin = " + mOriginalBottomMargin + " , topMargin = " + mHeadViewParams.topMargin + " , mOriginalMiddleMargin = " + mOriginalMiddleMargin + ", moveheight= " + moveheight + ", percent = " + percent);

                } else if (deltaY > 0 && mStatus == STATUS.EXPANDED) {
                    //下滑
                    mDirection = DIRECTION.DOWN;
                    //底部View处理
                    mBottomViewParams.topMargin += deltaY;
                    mBottomView.setLayoutParams(mBottomViewParams);

                    //头部View处理
                    float moveheight = mBottomViewParams.topMargin - Math.abs(mOriginalHeadMargin);//底部View移动的高度
                    float percent = moveheight / mOriginalMiddleMargin;//底部View移动的百分比
                    mHeadViewParams.topMargin = (int) (percent * mOriginalHeadMargin);//头部View移动的高度
                    mHeadView.setLayoutParams(mHeadViewParams);

                    //内容View处理，移动的高度等于头部View的高度
                    mContentViewParams.topMargin = mOriginalHeadMargin - (int) (percent * mOriginalHeadMargin);
                    mContentView.setLayoutParams(mContentViewParams);


                    Log.d("MyStickyLayout", "mOriginalBottomMargin = " + mOriginalBottomMargin + " , topMargin = " + mHeadViewParams.topMargin + " , mOriginalMiddleMargin = " + mOriginalMiddleMargin + ", percent = " + percent);
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float yVelocity = mVelocityTracker.getYVelocity();
                Log.d("MyStickyLayout", "yVelocity = " + yVelocity);
                if (mDirection == DIRECTION.UP) {
                    dealUpSlidAnimator(yVelocity);
                } else {
                    dealDownSlidAnimator(yVelocity);
                }
                break;
            default:
                break;
        }
        mLastY = y;
        return super.onTouchEvent(event);
    }

    /**
     * 处理上滑逻辑
     */
    private void dealUpSlidAnimator(float yVelocity) {
        if (isUpSlideScrollToSlide()||Math.abs(yVelocity)>VELOCITY_MAX) {
            mStatus = STATUS.EXPANDED;
            mHeadAnimator = ValueAnimator.ofInt(mHeadViewParams.topMargin, 0);
            mContentAnimator = ValueAnimator.ofInt(mContentViewParams.topMargin, mOriginalHeadMargin);
            mBottomAnimator = ValueAnimator.ofInt(mBottomViewParams.topMargin, Math.abs(mOriginalHeadMargin));
        } else {
            mHeadAnimator = ValueAnimator.ofInt(mHeadViewParams.topMargin, mOriginalHeadMargin);
            mContentAnimator = ValueAnimator.ofInt(mContentViewParams.topMargin, mOriginalContentMargin);
            mBottomAnimator = ValueAnimator.ofInt(mBottomViewParams.topMargin, mOriginalBottomMargin);
        }

        mHeadAnimator.removeAllUpdateListeners();
        mContentAnimator.removeAllUpdateListeners();
        mBottomAnimator.removeAllUpdateListeners();

        mHeadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mHeadViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mHeadView.setLayoutParams(mHeadViewParams);
            }
        });

        mContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mContentView.setLayoutParams(mContentViewParams);
            }
        });

        mBottomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBottomViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mBottomView.setLayoutParams(mBottomViewParams);
            }
        });

            mHeadAnimator.setDuration(ANIMATION_TIME).start();
            mContentAnimator.setDuration(ANIMATION_TIME).start();
            mBottomAnimator.setDuration(ANIMATION_TIME).start();

    }

    /**
     * 处理下滑逻辑
     */
    private void dealDownSlidAnimator(float yVelocity) {
        if (isDownSlideScrollToSlide()||Math.abs(yVelocity)>VELOCITY_MAX) {
            mStatus = STATUS.COLLAPSED;
            mHeadAnimator = ValueAnimator.ofInt(mHeadViewParams.topMargin, mOriginalHeadMargin);
            mContentAnimator = ValueAnimator.ofInt(mContentViewParams.topMargin, mOriginalContentMargin);
            mBottomAnimator = ValueAnimator.ofInt(mBottomViewParams.topMargin, mOriginalBottomMargin);
        } else {
            mHeadAnimator = ValueAnimator.ofInt(mHeadViewParams.topMargin, 0);
            mContentAnimator = ValueAnimator.ofInt(mContentViewParams.topMargin, mOriginalHeadMargin);
            mBottomAnimator = ValueAnimator.ofInt(mBottomViewParams.topMargin, Math.abs(mOriginalHeadMargin));
        }

        mHeadAnimator.removeAllUpdateListeners();
        mContentAnimator.removeAllUpdateListeners();
        mBottomAnimator.removeAllUpdateListeners();

        mHeadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mHeadViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mHeadView.setLayoutParams(mHeadViewParams);
            }
        });

        mContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mContentView.setLayoutParams(mContentViewParams);
            }
        });

        mBottomAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mBottomViewParams.topMargin = (int) valueAnimator.getAnimatedValue();
                mBottomView.setLayoutParams(mBottomViewParams);
            }
        });

            mHeadAnimator.setDuration(ANIMATION_TIME).start();
            mContentAnimator.setDuration(ANIMATION_TIME).start();
            mBottomAnimator.setDuration(ANIMATION_TIME).start();

    }

    /**
     * 上滑到两边的临界点
     */
    private boolean isUpSlideScrollToSlide() {
        //滑动距离超过底部View的topMargin的一半
        return mBottomViewParams.topMargin < mOriginalBottomMargin / 1.5;
    }

    /**
     * 下滑动到两边的临界点
     */
    private boolean isDownSlideScrollToSlide() {
        //滑动距离超过底部View的topMargin的一半
        return mBottomViewParams.topMargin > mOriginalBottomMargin / 3;
    }

    public interface TopTouchEventListener {
        boolean isBottomViewTop();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
        }
        super.onDetachedFromWindow();
    }
}
