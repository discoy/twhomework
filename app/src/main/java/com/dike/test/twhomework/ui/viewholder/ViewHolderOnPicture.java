package com.dike.test.twhomework.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import com.dike.test.twhomework.R;
import com.dike.test.twhomework.entity.DemoItem;
import com.dike.test.twhomework.ui.recyclerview.AViewHolder;
import com.dike.test.twhomework.ui.view.PictureView;
import com.dike.test.twhomework.utils.CommonUtil;

public class ViewHolderOnPicture extends AViewHolder<DemoItem>
{
    private PictureView mPv;
    private TextView mContentTv;
    public ViewHolderOnPicture()
    {
        CommonUtil.i("AViewHolder",""+hashCode());
    }

    @Override
    protected void onInitView(View content)
    {
        mPv = content.findViewById(R.id.id_tweets_photo_pv);
        mContentTv = content.findViewById(R.id.id_tweets_content_tv);
    }

    @Override
    protected View getContentView()
    {
        return null;
    }

    @Override
    public void recycle()
    {
        if(null != mPv)
        {
            mPv.recycle();
        }
    }

    @Override
    protected int getContentLayoutId()
    {
        return R.layout.viewholder_tweets_base;
    }

    @Override
    public void refresh(DemoItem item, int position, int totalCount, Object... payloads)
    {
        super.refresh(item, position, totalCount, payloads);

        CommonUtil.i("item.getPhotoUrls().lenght="+item.getPhotoUrls().length,"posi="+position);
        mPv.setPictures(item.getPhotoUrls());
        mContentTv.setText("item.getPhotoUrls().lenght="+item.getPhotoUrls().length);
    }
}
