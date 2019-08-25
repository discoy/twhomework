package com.dike.test.twhomework.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import com.dike.test.twhomework.BuildConfig;

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
}
