package cn.jiguang.imui.chatinput.utils;

import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;


public final class ViewUtil {

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static View formatViewWeight(View view,float weight) {
        if(view != null){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,weight);
            view.setLayoutParams(lp);
        }
        return view;
    }


}
