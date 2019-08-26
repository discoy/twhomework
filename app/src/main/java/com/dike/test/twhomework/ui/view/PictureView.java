package com.dike.test.twhomework.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.bumptech.glide.Glide;
import com.dike.test.twhomework.R;
import com.dike.test.twhomework.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

public class PictureView extends RelativeLayout
{
    private static final String TAG = "PictureView";
    private static final int DEF_MAX_PICTURE_COUNT = 9;
    private static final int COLUMN = 3;

    private static List<View> mViewPool = new ArrayList<>();
    private static int mMaxViewPoolSize = 50;

    private int mMaxPictureCount = DEF_MAX_PICTURE_COUNT;
    private int mRowMargin = 10;
    private int mColumnMargin = 10;
    private String[] mPhotoUrls;

    public PictureView(Context context)
    {
        super(context);
        init();
    }

    public PictureView(Context context, AttributeSet attrs)
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
                if(0 != getWidth())
                {
                    addPhotoView(mPhotoUrls);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
//        if(null == urls || 0 == urls.length)
//        {
//            return;
//        }
        this.mPhotoUrls = urls;
        if(0 < getWidth())
        {
            addPhotoView(urls);
        }
//        measure();
//        int validPictureCount = Math.min(mMaxPictureCount,urls.length);
//        if(1 == validPictureCount)
//        {
//            ImageView imageView = new ImageView(getContext());
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
//                    ,LinearLayout.LayoutParams.WRAP_CONTENT);
//            addView(imageView,layoutParams);
//
//            Glide.with(this).load(R.mipmap.ic_launcher).into(imageView);
//        }
//        else
//        {
////            int row = (int) Math.ceil(validPictureCount * 1.0 / COLUMN);
////            LinearLayout layoutRow;
//            ImageView itemIv;
//            for(int i = 0; i < validPictureCount; i++)
//            {
//                itemIv = getItemView((int) Math.floor(i * 1.0 / COLUMN),i % COLUMN);
//                addView(itemIv);
//                Glide.with(this).load(R.mipmap.ic_launcher).into(itemIv);
//            }
//        }
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

            Glide.with(this).load(R.mipmap.ic_launcher).into(imageView);
        }
        else
        {
//            int row = (int) Math.ceil(validPictureCount * 1.0 / COLUMN);
//            LinearLayout layoutRow;
            ImageView itemIv;
            for(int i = 0; i < validPictureCount; i++)
            {
                itemIv = getItemView((int) Math.floor(i * 1.0 / COLUMN),i % COLUMN);
                addView(itemIv);
                Glide.with(this).load(R.mipmap.ic_launcher).into(itemIv);
            }
        }
    }


    private void measure()
    {

        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null)
        {
            // 标签默认宽度占满parent
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            setLayoutParams(layoutParams);
        }

        // 测量高度
        int heightMode = View.MeasureSpec.getMode(layoutParams.height);
        int heightSize = View.MeasureSpec.getSize(layoutParams.height);

        if (heightMode == View.MeasureSpec.UNSPECIFIED)
        {
            heightMode = View.MeasureSpec.EXACTLY;
        }
        View parent = null == getParent() ? null : (View) getParent();
        if(null == parent)
        {
            return;
        }
        int maxHeight = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
        if (heightSize > maxHeight)
        {
            heightSize = maxHeight;
        }


        int ws = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
        int hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        measure(ws, hs);

//        int width = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int height = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        //调用measure方法之后就可以获取宽高
//        measure(width, height);
    }

    private int getRowItemWidth()
    {
        return (getWidth() - getPaddingLeft() - getPaddingRight()
                - (COLUMN + 1) * mColumnMargin) / COLUMN;
    }

    private LinearLayout.LayoutParams getRowLayoutParams()
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
                ,getRowItemWidth());
        return layoutParams;
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
        CommonUtil.i(TAG,"onMeasure");
    }


}
