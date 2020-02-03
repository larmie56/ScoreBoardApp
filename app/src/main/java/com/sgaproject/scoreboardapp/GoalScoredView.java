package com.sgaproject.scoreboardapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;

import java.util.List;

/**
 * TODO: document your custom view class.
 */

public class GoalScoredView extends View {

    private Paint mFillPaint;
    private float mShapeSize;
    private float mRadius;
    private float mSpacing;
    private Rect[] mGoalRect;
    private GoalScoredAccessibilityHelper mAccessibilityHelper;
    private int[] mGoalScoredArray;

    public GoalScoredView(Context context) {
        super(context);
        init(null, 0);
    }

    public GoalScoredView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GoalScoredView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setUpGoalScoredView(int[] goalScoredArray) {
        mGoalScoredArray = goalScoredArray;
    }


    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GoalScoredView, defStyle, 0);

        int color = a.getColor(R.styleable.GoalScoredView_goal_color, getResources().getColor(R.color.goalColor));

        a.recycle();

        setFocusable(true);

        mAccessibilityHelper = new GoalScoredAccessibilityHelper(this);
        ViewCompat.setAccessibilityDelegate(this, mAccessibilityHelper);


        if (isInEditMode()) {
            setUpEditValues();
        }

        mShapeSize = 120f;
        mSpacing = 24f;
        mRadius = mShapeSize/2;

        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setColor(color);
    }

    private void setUpEditValues() {
        setUpGoalScoredView(new int[7]);
    }

    private void goalRect(int width) {
        int availableWidth = width - getPaddingStart() - getPaddingEnd();
        int goalShapesFit = (int) (availableWidth/(mShapeSize + mSpacing));
        int maxGoalShapesFit = Math.min(goalShapesFit, mGoalScoredArray.length);

        mGoalRect = new Rect[mGoalScoredArray.length];
        for (int index = 0; index < mGoalScoredArray.length; index++) {
            int column = index % maxGoalShapesFit;
            int rows = index/maxGoalShapesFit;
            int x = getPaddingStart() + (int) (column * (mShapeSize + mSpacing));
            int y = getPaddingTop () + (int) ((rows * (mShapeSize + mSpacing)));

            mGoalRect[index] = new Rect(x, y, x + (int) mShapeSize, y + (int) mShapeSize);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int viewPosition = findItemAtPosition(event.getX(), event.getY());
                return true;
        }

        return super.onTouchEvent(event);
    }

    private int findItemAtPosition(float x, float y) {
        int viewPosition = -1;

        for (int index = 0; index < mGoalRect.length; index++) {
            if (mGoalRect[index].contains((int) x, (int) y)) {
                viewPosition = index;
                break;
            }
        }
        return viewPosition;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int index = 0; index < mGoalRect.length; index++) {
            float centerX = mGoalRect[index].centerX();
            float centerY = mGoalRect[index].centerY();

            canvas.drawCircle(centerX, centerY, mRadius, mFillPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int constrainedWidth = MeasureSpec.getSize(widthMeasureSpec);

        int availableWidth = constrainedWidth - getPaddingStart() - getPaddingEnd();
        int horizontalGoalFit = (int) (availableWidth / (mShapeSize + mSpacing));
        int columns= Math.min(mGoalScoredArray.length, horizontalGoalFit);
        int rows = ((mGoalScoredArray.length - 1)/columns) + 1;

        int width = (int)((columns * (mShapeSize + mSpacing)) - mSpacing);
        width += getPaddingStart() + getPaddingEnd();
        int height = (int) ((rows * (mShapeSize + mSpacing)) - mSpacing);
        height += getPaddingBottom() + getPaddingTop();

        int resolvedWidth = resolveSizeAndState(width, widthMeasureSpec, 0);
        int resolvedHeight = resolveSizeAndState(height, heightMeasureSpec, 0);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        goalRect(w);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        mAccessibilityHelper.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mAccessibilityHelper.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return mAccessibilityHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    private class GoalScoredAccessibilityHelper extends ExploreByTouchHelper {

/**
         * Constructs a new helper that can expose a virtual view hierarchy for the
         * specified host view.
         *
         * @param host view whose virtual view hierarchy is exposed by this helper
         */

        public GoalScoredAccessibilityHelper(@NonNull View host) {
            super(host);
        }

        @Override
        protected int getVirtualViewAt(float x, float y) {
            return findItemAtPosition(x, y);
        }

        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            for (int index = 0; index < mGoalRect.length; index++) {
                virtualViewIds.add(index);
            }
        }

        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, @NonNull AccessibilityNodeInfoCompat node) {
            node.setFocusable(true);
            node.setBoundsInParent(mGoalRect[virtualViewId]);
            int goalScored = virtualViewId + 1;
            node.setContentDescription("Goal(s) scored is equal to" + goalScored);
        }

        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, @Nullable Bundle arguments) {
            return false;
        }
    }

}