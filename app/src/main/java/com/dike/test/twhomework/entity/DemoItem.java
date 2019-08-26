package com.dike.test.twhomework.entity;

import com.dike.test.twhomework.ui.recyclerview.TItem;

public class DemoItem extends TItem
{
    private String[] mPhotoUrls;
    public DemoItem()
    {
        super();
    }

    public void setPhotoUrls(String[] mPhotoUrls)
    {
        this.mPhotoUrls = mPhotoUrls;
    }

    public String[] getPhotoUrls()
    {
        return mPhotoUrls;
    }
}
