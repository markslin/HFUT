package cn.edu.hfut.xc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import cn.edu.hfut.xc.hfut.R;

/**
 * Created by MarksLin on 2015/10/26 0026.
 */
public class ColorProgressBar extends ProgressBar {
    public ColorProgressBar(Context context) {
        super(context);
    }

    public ColorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorProgressBar, 0, 0);
        final int color = a.getColor(R.styleable.ColorProgressBar_cpb_color, 0xFFABA9A9);
        Drawable drawable = getIndeterminateDrawable();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);//MULTIPLY/SRC_ATOP/SRC_IN
        setIndeterminateDrawable(drawable);
    }

    public ColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIndeterminateDrawableColor(int color) {
        Drawable drawable = getIndeterminateDrawable();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);//MULTIPLY/SRC_ATOP/SRC_IN
        setIndeterminateDrawable(drawable);
    }
}
