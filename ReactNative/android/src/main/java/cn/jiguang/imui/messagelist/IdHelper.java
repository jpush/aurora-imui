package cn.jiguang.imui.messagelist;

import android.content.Context;

import java.lang.reflect.Field;

public class IdHelper {

    public static int getLayout(Context context, String layoutName) {
        return context.getResources().getIdentifier(layoutName, "layout",
                context.getApplicationContext().getPackageName());
    }

    public static int getViewID(Context context, String IDName) {
        return context.getResources().getIdentifier(IDName, "id",
                context.getApplicationContext().getPackageName());
    }

    public static int getDrawable(Context context, String drawableName) {
        return context.getResources().getIdentifier(drawableName, "drawable",
                context.getApplicationContext().getPackageName());
    }

    public static int getAttr(Context context, String attrName) {
        return context.getResources().getIdentifier(attrName, "attr",
                context.getApplicationContext().getPackageName());
    }

    public static int getString(Context context, String stringName) {
        return context.getResources().getIdentifier(stringName, "string",
                context.getApplicationContext().getPackageName());
    }

    public static int getStyle(Context context, String styleName) {
        return context.getResources().getIdentifier(styleName, "style",
                context.getApplicationContext().getPackageName());
    }

    public static int[] getResourceDeclareStyleableIntArray(Context context, String name) {
        try {
            Field[] fields2 = Class.forName(context.getPackageName() + ".R$styleable").getFields();

            //browse all fields
            for (Field f : fields2) {
                //pick matching field
                if (f.getName().equals(name)) {
                    //return as int array
                    return (int[]) f.get(null);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }

    public static int getAnim(Context context, String animName) {
        return context.getResources().getIdentifier(animName, "anim",
                context.getApplicationContext().getPackageName());
    }
}