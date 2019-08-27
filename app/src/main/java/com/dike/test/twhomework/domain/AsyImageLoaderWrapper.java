package com.dike.test.twhomework.domain;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dike.test.twhomework.R;

public class AsyImageLoaderWrapper
{

    public static void displayImage(Object tag,String uri,ImageView imageView)
    {
        RequestManager requests = getGlideRequests(tag);
        if(null != requests)
        {
            requests.load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                        .into(imageView);

        }
    }

    private static RequestManager getGlideRequests(Object tag)
    {
        //use glide if tag is intance of context or fragment
        RequestManager requests = null;
        if(Fragment.class.isInstance(tag))
        {
            Fragment fragment = (Fragment) tag;
            if(null == fragment.getActivity())
            {
                return null;
            }
            requests = Glide.with(fragment);
        }
        else if(FragmentActivity.class.isInstance(tag))
        {
            requests = Glide.with((FragmentActivity) tag);
        }
        else if(Activity.class.isInstance(tag))
        {
            requests = Glide.with((Activity) tag);
        }
        else if(View.class.isInstance(tag))
        {
            requests = Glide.with((View) tag);
        }
        else if(Context.class.isInstance(tag))
        {
            requests = Glide.with((Context) tag);
        }
        return requests;
    }


}
