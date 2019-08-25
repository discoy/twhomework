package com.dike.test.twhomework.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;


public abstract class AViewHolder<T extends TItem>
{
    private View mContentView;
    private TItem mCurItem;
    private int mCurPos;


    public View getView(LayoutInflater mInflater)
    {
        if(null != mContentView)
        {
            return mContentView;
        }
        mContentView = getContentView();
        int resId = getContentLayoutId();
        if(null != mInflater && null == mContentView)
        {
            mContentView = mInflater.inflate(resId, null);
        }
        onInitView(mContentView);
        return mContentView;
    }

    public void refresh(T item, int position, int totalCount,Object... payloads)
    {
        mCurItem = item;
        mCurPos = position;
    }


    /**
     * 初始化view，子类必须实现
     * @param content root content view
     */
    protected abstract void onInitView(View content);

    /**
     * 返回item内容布局view。优先使用
     * @return item内容布局view。
     */
    protected abstract View getContentView();

    /**
     * 获取item内容的布局layoutid。当{{@link #getContentView()}}返回为null时使用此返回值
     * @return item内容布局layoutid或-1
     */
    protected abstract int getContentLayoutId();
}
