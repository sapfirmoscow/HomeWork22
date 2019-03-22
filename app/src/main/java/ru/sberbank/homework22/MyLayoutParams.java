package ru.sberbank.homework22;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

public class MyLayoutParams extends ViewGroup.MarginLayoutParams {
    public static final int POSITION_MIDDLE = 0;
    public static final int POSITION_LEFT = 1;
    public static final int POSITION_RIGHT = 2;

    public int gravity = Gravity.TOP | Gravity.START;

    public int position = POSITION_MIDDLE;

    public MyLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
        TypedArray a = null;
        try {
            a = c.obtainStyledAttributes(attrs, R.styleable.MyCustomLayoutLP);
            gravity = a.getInt(R.styleable.MyCustomLayoutLP_android_layout_gravity, gravity);
            position = a.getInt(R.styleable.MyCustomLayoutLP_layout_position, position);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }
}
