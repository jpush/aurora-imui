package cn.jiguang.imui.utils;

import android.content.Context;
import android.util.TypedValue;


public class DisplayUtil {
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
