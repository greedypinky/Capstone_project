package com.project.capstone_stage2.util;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ritalaw on 2018-03-28.
 */

/**
 * Add the hide and show behaviour for FAB
 */
public class FabButtonBehavior extends FloatingActionButton.Behavior {
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        // Ensure we react to vertical scrolling only
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        Log.d("OnNestedScroll", "what is dyConsumed?" + dyConsumed);

        // vertical pixels consumed by the target's own scrolling operation > 0
        if (dyConsumed > 0) {
            Log.d("FAB_Behavior", "Scroll up - Hide FAB");
            child.hide();
        } else if (dyConsumed < 0) {
            Log.d("FAB_Behavior", "Scroll down - Hide FAB");
            child.hide();
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
        Log.d("FAB_Behavour", "Stop scrolling - Show FAB");
        child.show();
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    public FabButtonBehavior() {
        super();
    }

    public FabButtonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}

