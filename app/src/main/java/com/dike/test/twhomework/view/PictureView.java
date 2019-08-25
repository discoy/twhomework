package com.dike.test.twhomework.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.dike.test.twhomework.R;
import com.dike.test.twhomework.utils.CommonUtil;

public class PictureView extends LinearLayout
{
    private static final String TAG = "PictureView";
    private static final int DEF_MAX_PICTURE_COUNT = 9;
    private static final int COLUMN = 3;

    private int mMaxPictureCount = DEF_MAX_PICTURE_COUNT;
    private int mRowMargin = 10;
    private int mColumnMargin = 10;

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
        setOrientation(VERTICAL);
        setBackgroundColor(Color.GRAY);
    }



    public void setMaxPictureCount(int count)
    {
        mMaxPictureCount = Math.min(count,DEF_MAX_PICTURE_COUNT);
    }

    public void setPictures(String[] urls)
    {
        if(null == urls || 0 == urls.length)
        {
            return;
        }
        int validPictureCount = Math.min(mMaxPictureCount,urls.length);
        if(1 == validPictureCount)
        {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                    ,LinearLayout.LayoutParams.WRAP_CONTENT);
            addView(imageView,layoutParams);

            Glide.with(this).load(R.mipmap.ic_launcher).into(imageView);
        }
        else
        {
            int column = 3;
            int row = (int) Math.ceil(validPictureCount * 1.0 / column);
            LinearLayout layoutRow;
            ImageView itemIv;
            for(int i = 0; i < row; i ++)
            {
                layoutRow = new LinearLayout(getContext());
                layoutRow.setOrientation(HORIZONTAL);
                for(int j = 0; j < column; j++)
                {
                    itemIv = getItemView();
                    layoutRow.addView(itemIv);
                    Glide.with(this).load(R.mipmap.ic_launcher).into(itemIv);
                }
                addView(layoutRow,getRowLayoutParams());
            }
        }
    }

    private int getRowItemWidth()
    {
        return (CommonUtil.getScreenWidth(getContext()) - getPaddingLeft() - getPaddingRight()
                - COLUMN * mRowMargin) / COLUMN;
    }

    private LinearLayout.LayoutParams getRowLayoutParams()
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
                ,getRowItemWidth());
        return layoutParams;
    }

    private ImageView getItemView()
    {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundColor(Color.RED);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getRowItemWidth()
                ,getRowItemWidth());
        layoutParams.leftMargin = mRowMargin;
        imageView.setLayoutParams(layoutParams);
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
