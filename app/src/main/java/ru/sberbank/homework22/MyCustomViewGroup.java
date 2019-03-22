package ru.sberbank.homework22;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

public class MyCustomViewGroup extends ViewGroup {


    private int mLeftWidth;
    private int mRightWidth;
    private Rect mTmpContainerRect = new Rect();
    private Rect mTmpChildRect = new Rect();

    public MyCustomViewGroup(Context context) {
        super(context);
    }

    public MyCustomViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        mLeftWidth = 0;
        mRightWidth = 0;

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                final MyLayoutParams lp = (MyLayoutParams) child.getLayoutParams();
                if (lp.position == MyLayoutParams.POSITION_LEFT) {
                    mLeftWidth += Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else if (lp.position == MyLayoutParams.POSITION_RIGHT) {
                    mRightWidth += Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                } else {
                    maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                }
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        maxWidth += mLeftWidth + mRightWidth;

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        int leftPos = getPaddingLeft();
        int rigthPos = r - l - getPaddingRight();

        final int middleLeft = leftPos + mLeftWidth;
        final int middleRight = leftPos + mRightWidth;

        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int bottomChild = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final MyLayoutParams lp = (MyLayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                if (lp.position == MyLayoutParams.POSITION_LEFT) {
                    mTmpContainerRect.left = leftPos + lp.leftMargin;
                    mTmpContainerRect.right = leftPos + width + lp.rightMargin;
                    leftPos = mTmpContainerRect.right;
                } else if (lp.position == MyLayoutParams.POSITION_RIGHT) {
                    mTmpContainerRect.right = rigthPos - lp.rightMargin;
                    mTmpContainerRect.left = rigthPos - width - lp.leftMargin;
                    rigthPos = mTmpContainerRect.left;
                } else {
                    mTmpContainerRect.left = middleLeft + lp.leftMargin;
                    mTmpContainerRect.right = middleRight - lp.rightMargin;
                }
                mTmpContainerRect.top = bottomChild + lp.topMargin;
                mTmpContainerRect.bottom = parentBottom - lp.bottomMargin;

                Gravity.apply(lp.gravity, width, height, mTmpContainerRect, mTmpChildRect);

                child.layout(mTmpChildRect.left, mTmpChildRect.top, mTmpChildRect.right, mTmpChildRect.bottom);
                bottomChild = child.getBottom();
            }
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MyLayoutParams(getContext(), null);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MyLayoutParams(getContext(), null);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParams(getContext(), attrs);
    }
}
