package zeng.fanda.recyclerviewdemo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import zeng.fanda.recyclerviewdemo.R;
import zeng.fanda.recyclerviewdemo.utils.DpUtil;

/**
 * 默认分隔线实现类只支持布局管理器为 LinearLayoutManager
 *
 * @author 曾凡达
 * @date 2018/5/29
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private boolean mShowLastLine;
    private int mSpanSpace = 1;
    private int mLeftPadding;
    private int mRightPadding;

    public LinearItemDecoration(int span, int leftPadding, int rightPadding, int color, boolean show){
        mSpanSpace = span;
        mShowLastLine = show;
        mLeftPadding = leftPadding;
        mRightPadding = rightPadding;
        mDivider = new ColorDrawable(color);
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int count = mShowLastLine ? parent.getAdapter().getItemCount() : parent.getAdapter().getItemCount() - 1;
        if (isVertical(parent)) {
            if (parent.getChildAdapterPosition(view) < count) {
                outRect.set(0, 0, 0, mSpanSpace);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            if (parent.getChildAdapterPosition(view) < count) {
                outRect.set(0, 0, mSpanSpace, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    private boolean isVertical(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int orientation = ((LinearLayoutManager) layoutManager) .getOrientation();
            return orientation == LinearLayoutManager.VERTICAL;
        }
        return false;
    }

    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (isVertical(parent)) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 在LinearLayoutManager方向为Vertical时，画分隔线
     */
    public void drawVertical(Canvas canvas, RecyclerView parent) {
        //★分隔线的左边 = paddingLeft值
        final int left = parent.getPaddingLeft()+mLeftPadding;
        //★分隔线的右边 = RecyclerView 宽度－paddingRight值
        final int right = parent.getWidth() - parent.getPaddingRight()-mRightPadding;

        //分隔线不在RecyclerView的padding那一部分绘制
        //★分隔线数量=item数量
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            //确定是第几个item
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //★分隔线的上边 = item的底部 + item根标签的bottomMargin值
            final int top = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
            //★分隔线的下边 = 分隔线的上边 + 分隔线本身高度
            final int bottom = top + mSpanSpace;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    /**
     * 在LinearLayoutManager方向为Horizontal时，画分隔线
     */
    public void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin+ Math.round(child.getTranslationY());
            final int right = left + mSpanSpace;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }


    /**
     * Builder模式
     * */
    public static class Builder{

        private Context mContext;
        private int mSpanSpace;
        private boolean mShowLastLine;
        private int mLeftPadding;
        private int mRightPadding;
        private Resources mResources;
        private int mColor;

        public Builder(Context context){
            mContext = context;
            mResources = mContext.getResources();
            mSpanSpace = (int) DpUtil.dp2px(mContext,1);
            mLeftPadding = 0;
            mRightPadding = 0;
            mShowLastLine = false;
            mColor =mResources.getColor(R.color.item_divider_bg);
        }

        /**
         * 设置分割线宽（高）度
         */
        public Builder setDivideHeight(int divideHeight) {
            mSpanSpace = (int) DpUtil.dp2px(mContext,divideHeight);
            return this;
        }

        /**
         * 设置左右间距
         */
        public Builder setPadding(int padding) {
            setLeftPadding(padding);
            setRightPadding(padding);
            return this;
        }

        /**
         * 设置左右间距
         */
        public Builder setPaddingResource(@DimenRes int resource) {
            setLeftPaddingResource(resource);
            setRightPaddingResource(resource);
            return this;
        }

        /**
         * 通过资源id设置右间距
         */
        public Builder setRightPaddingResource(@DimenRes int resource) {
            mRightPadding = mResources.getDimensionPixelSize(resource);
            return this;
        }

        /**
         * 通过资源id设置右间距
         */
        public Builder setLeftPaddingResource(@DimenRes int resource) {
            mLeftPadding = mResources.getDimensionPixelSize(resource);
            return this;
        }


        /**
         * 设置左间距
         */
        public Builder setLeftPadding(int padding) {
            mLeftPadding = (int) DpUtil.dp2px(mContext,padding);
            return this;
        }

        /**
         * 设置右间距
         */
        public Builder setRightPadding(int padding) {
            mRightPadding = (int) DpUtil.dp2px(mContext,padding);
            return this;
        }


        /**
         * 通过资源id设置颜色
         */
        public Builder setColorResource(@ColorRes int resource) {
            setColor(ContextCompat.getColor(mContext,resource));
            return this;
        }

        /**
         * 设置颜色
         */
        public Builder setColor(@ColorInt int color) {
            mColor = color;
            return this;
        }

        /**
         * 是否最后一条显示分割线
         * */
        public Builder setShowLastLine(boolean show){
            mShowLastLine = show;
            return this;
        }

        /**
         * Instantiates a LinearItemDecoration with the specified parameters.
         * @return a properly initialized LinearItemDecoration instance
         */
        public LinearItemDecoration build() {
            return new LinearItemDecoration(mSpanSpace,mLeftPadding,mRightPadding,mColor,mShowLastLine);
        }

    }


}
