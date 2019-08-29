package com.dike.test.twhomework.ui.view;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dike.test.twhomework.domain.AsyImageLoaderWrapper;
import com.dike.test.twhomework.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class PhotoView extends RelativeLayout
{
    private static final String TAG = "PictureView";
    private static final int DEF_MAX_PICTURE_COUNT = 9;
    private static final int COLUMN = 3;

    private static List<View> mViewPool = new ArrayList<>();
    private static int mMaxViewPoolSize = 50;

    private  int mAvailableWidth;
    private int mMaxPictureCount = DEF_MAX_PICTURE_COUNT;
    private int mRowMargin = 10;
    private int mColumnMargin = 10;
    private String[] mPhotoUrls;

    public PhotoView(Context context)
    {
        super(context);
        init();
    }

    public PhotoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setBackgroundColor(Color.WHITE);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                testMeasure("onGlobalLayout");
                CommonUtil.i("setPictures-on","getWidth="+ mAvailableWidth +"hashcode="+hashCode());
                if(mAvailableWidth > 0)
                {
                    addPhotoView(mPhotoUrls);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else
                {
                    mAvailableWidth = getWidth();
                }
            }
        });
    }


    private synchronized static boolean addViewToPool(View view)
    {
        int size = mViewPool.size();
        if(size >= mMaxViewPoolSize)
        {
            return false;
        }
        else
        {
            mViewPool.add(view);
            return true;
        }
    }

    private synchronized static View getViewFromPool()
    {
        int size = mViewPool.size();
        if(size > 0)
        {
            mMaxViewPoolSize = size - 1;
            return mViewPool.remove(size - 1);
        }
        return null;
    }

    public void recycle()
    {
        int childCount = getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            if(!addViewToPool(getChildAt(i)))
            {
                break;
            }
        }
        removeAllViews();
    }



    public void setMaxPictureCount(int count)
    {
        mMaxPictureCount = Math.min(count,DEF_MAX_PICTURE_COUNT);
    }

    public void setPictures(String[] urls)
    {
        testMeasure("setPictures");
        this.mPhotoUrls = urls;

        CommonUtil.i("setPictures","mAvailableWidth="+ mAvailableWidth +",urls="+urls.length+"hashcode="+hashCode());
        if(0 < mAvailableWidth)
        {
            addPhotoView(urls);
        }
        else
        {
            CommonUtil.i("setPictures","getWidth=0,urls="+urls.length+"hashcode="+hashCode());
        }
    }

    private void testMeasure(String tag)
    {
//        int  widthSpec = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
//        int  heightSpec = View.MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
//        ((ViewGroup)getParent()).measure(widthSpec,heightSpec);
        int   height = getMeasuredHeight();
        int   width = getWidth();
        CommonUtil.i("testMeasure"+tag,"height="+height+"&width="+width +"&getAvailableWidth="+getAvailableWidth()+"&hashcode="+hashCode());
    }

    private int getAvailableWidth()
    {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if(layoutParams.width > 0)
        {
            return layoutParams.width;
        }
        else
        {
            int paddingHorizontal = 0;
            ViewParent parent = getParent();
            while (View.class.isInstance(parent))
            {

                paddingHorizontal += ((View)parent).getPaddingLeft() + ((View)parent).getPaddingRight();
                parent = parent.getParent();
            }

            return CommonUtil.getScreenWidth(getContext()) - paddingHorizontal;
        }

    }

    private void addPhotoView(String[] urls)
    {
        if(null == urls || 0 == urls.length)
        {
            return;
        }
        int validPictureCount = Math.min(mMaxPictureCount,urls.length);
        if(1 == validPictureCount)
        {
            ImageView imageView = getItemView(-1,-1);
            addView(imageView);
            AsyImageLoaderWrapper.displayImage(this,urls[0],imageView);
        }
        else
        {
            ImageView itemIv;
            for(int i = 0; i < validPictureCount; i++)
            {
                itemIv = getItemView((int) Math.floor(i * 1.0 / COLUMN),i % COLUMN);
                addView(itemIv);
                AsyImageLoaderWrapper.displayImage(this,urls[i],itemIv);
            }
        }
    }



    private int getRowItemWidth()
    {
        return (mAvailableWidth - getPaddingLeft() - getPaddingRight()
                - (COLUMN + 1) * mColumnMargin) / COLUMN;
    }

    private ImageView getItemView(int rowIndex,int columnIndex)
    {
        View view = getViewFromPool();
        ImageView imageView;
        if(null != view)
        {
            imageView = (ImageView) view;
        }
        else
        {
            imageView = new ImageView(getContext());
            imageView.setBackgroundColor(Color.GRAY);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        if(rowIndex < 0 && columnIndex < 0)
        {
            LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    ,LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
        }
        else
        {
            int itemWidth = getRowItemWidth();
            LayoutParams layoutParams = new LayoutParams(itemWidth,itemWidth);
            layoutParams.leftMargin = mColumnMargin + columnIndex * (mColumnMargin + itemWidth);
            layoutParams.topMargin = rowIndex * (mRowMargin + itemWidth);
            imageView.setLayoutParams(layoutParams);

        }

        return imageView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        CommonUtil.i(TAG,"onLayout");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CommonUtil.i(TAG,"onMeasure=width="+getMeasuredWidth()+"&height="+getMeasuredHeight());
    }


}
