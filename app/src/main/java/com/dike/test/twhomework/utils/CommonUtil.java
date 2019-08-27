package com.dike.test.twhomework.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.dike.test.twhomework.BuildConfig;

import java.util.Map;

public class CommonUtil
{
    public static boolean IS_DEBUG = BuildConfig.LOG_DEBUG;
    public static void i(String tag,String info)
    {
        if(IS_DEBUG)
        {
            Log.i(tag,info);
        }
    }

    public static int getScreenWidth(Context context)
    {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int dip2px(Context context,float dip)
    {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context
                .getResources().getDisplayMetrics()));
    }

    public static <T> T getValueFromMap(Map<String,Object> map, String key, T def)
    {
        Object value = map.get(key);
        if(null != value)
        {
            return (T) value;
        }
        return def;
    }
}
