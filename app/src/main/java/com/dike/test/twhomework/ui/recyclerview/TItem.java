package com.dike.test.twhomework.ui.recyclerview;

public class TItem
{
    protected boolean mIsVisible;
    private int mViewType;

    public TItem()
    {
        mViewType = 0;
        mIsVisible = true;
    }


    public void setVisible(boolean mIsVisible)
    {
        this.mIsVisible = mIsVisible;
    }

    public boolean isVisible()
    {
        return mIsVisible;
    }

    public int getViewType()
    {
        return mViewType;
    }

    public void setViewType(int mViewType)
    {
        this.mViewType = mViewType;
    }
}
