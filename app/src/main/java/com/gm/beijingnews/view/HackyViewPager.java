package com.gm.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import uk.co.senab.photoview.gestures.GestureDetector;
import uk.co.senab.photoview.gestures.OnGestureListener;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {
    private GestureDetector mGestureDetector;

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector() {
            @Override
            public boolean onTouchEvent(MotionEvent ev) {
                return false;
            }

            @Override
            public boolean isScaling() {
                return false;
            }

            @Override
            public boolean isDragging() {
                return true;
            }

            @Override
            public void setOnGestureListener(OnGestureListener listener) {

            }
        };
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }
}
