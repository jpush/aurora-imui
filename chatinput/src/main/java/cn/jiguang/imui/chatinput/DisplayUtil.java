package cn.jiguang.imui.chatinput;

import android.content.Context;

/**
 * Created by hevin on 13/03/2017.
 */
public class DisplayUtil {
    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
