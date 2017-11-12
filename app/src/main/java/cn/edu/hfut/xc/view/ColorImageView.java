package cn.edu.hfut.xc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.edu.hfut.xc.hfut.R;

/**
 * Created by MarksLin on 2015/10/17 0017.
 */
public class ColorImageView extends ImageView {
    public ColorImageView(Context context) {
        super(context);
    }

    public ColorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorImageView, defStyleAttr, 0);
        final int color = a.getColor(R.styleable.ColorImageView_src_color, 0xFFABA9A9);
        Drawable drawable = getDrawable();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);//MULTIPLY/SRC_ATOP/SRC_IN
        setImageDrawable(drawable);
    }

    public void setImageColor(int color) {
        Drawable drawable = getDrawable();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);//MULTIPLY/SRC_ATOP/SRC_IN
        setImageDrawable(drawable);
    }
}
