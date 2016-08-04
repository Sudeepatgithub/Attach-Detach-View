package com.example.sudeepsrivastava.removeattach;

import android.content.Context;
import android.support.v4.widget.Space;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sudeep.srivastava on 5/3/2016.
 */

public class AttachDetach {

    private ViewGroup originalParent, newParent;
    private int originalIndex, scrollY;
    private View view;
    private Context context;
    private ViewGroup.LayoutParams paramsNew;
    private ViewGroup.LayoutParams paramsOriginal;

    /**
     * @param view           View that will switch parent ViewGroup
     * @param originalParent Default ViewGroup
     * @param newParent      ViewGroup in which view will be moved
     * @param scrollY        Number of Pixels buffer
     * @param context
     * @param paramsNew      LayoutParams for this view in the new ViewGroup
     */
    public AttachDetach(View view, ViewGroup originalParent, ViewGroup newParent,
                        int scrollY, Context context, ViewGroup.LayoutParams paramsNew) {
        this.originalParent = originalParent;
        this.newParent = newParent;
        this.context = context;
        //this.scrollY = dpToPx(scrollY);
        this.scrollY = scrollY;
        this.view = view;
        this.paramsNew = paramsNew;
        originalIndex = originalParent.indexOfChild(view);
    }

    /**
     * Check if the Scrollable view has scrolled enough number of pixels to shift it to the new Parent
     *
     * @param scrolled Number of Pixels
     */
    public void checkIfScroll(int scrolled) {
        if (scrolled > scrollY) {
            if (view.getParent() != newParent) {
                removeViewFromParent(originalParent);
                addViewToParent(newParent, 1);
            }
        } else {
            if (view.getParent() != originalParent) {
                removeViewFromParent(newParent);
                addViewToParent(originalParent, 0);
            }
        }
    }

    /**
     * Adds view to the passed parent
     *
     * @param parent ViewGroup in which view is to be attached
     * @param oldNew Set 1 while attaching it to new Parent else pass 0
     */
    private void addViewToParent(ViewGroup parent, int oldNew) {

        View space = parent.findViewWithTag("space");

        if (space != null)         // Check if space is present, remove it before adding the previous View
            parent.removeView(space);

        if (oldNew == 1) {
            parent.addView(view, paramsNew);
        } else {

            if (paramsOriginal != null) {
                parent.addView(view, originalIndex, paramsOriginal);
            } else {
                parent.addView(view, originalIndex);
            }
        }
    }

    /**
     * Function to set the Original LayoutParams of the view,
     * Call it when the view is drawn on screen
     *
     * @param layoutParams
     */

    public void setOriginalParams(ViewGroup.LayoutParams layoutParams) {
        paramsOriginal = layoutParams;
    }


    private void removeViewFromParent(ViewGroup parent) {

        //Create a space widget of similar dimensions as the view to be removed

        Space space = new Space(context);
        space.setTag("space");
        space.setMinimumHeight(view.getMeasuredHeight());
        space.setMinimumWidth(view.getMeasuredWidth());

        int index = parent.indexOfChild(view);  //Index where space is to be added
        parent.addView(space, index);

        parent.removeView(view);
    }

    /**
     * Use to convert DP into Pixels if required
     *
     * @param dp
     * @return
     */

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
