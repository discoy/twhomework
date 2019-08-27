package com.dike.test.twhomework.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import com.dike.test.twhomework.R;
import com.dike.test.twhomework.entity.TweetsItem;
import com.dike.test.twhomework.ui.recyclerview.AViewHolder;
import com.dike.test.twhomework.ui.view.CommentView;
import com.dike.test.twhomework.ui.view.PhotoView;
import com.dike.test.twhomework.utils.CommonUtil;

public class ViewHolderContent extends AViewHolder<TweetsItem>
{
    private PhotoView mPv;
    private TextView mContentTv;
    private CommentView mCommentView;
    private TextView mNickNameTv;
    public ViewHolderContent()
    {
        CommonUtil.i("AViewHolder",""+hashCode());
    }

    @Override
    protected void onInitView(View content)
    {
        mPv = content.findViewById(R.id.id_tweets_photo_pv);
        mContentTv = content.findViewById(R.id.id_tweets_content_tv);
        mCommentView = content.findViewById(R.id.id_tweets_sender_cv);
        mNickNameTv = content.findViewById(R.id.id_tweets_nickname_tv);
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
        if(null != mCommentView)
        {
            mCommentView.recycle();
        }
    }

    @Override
    protected int getContentLayoutId()
    {
        return R.layout.viewholder_tweets_content;
    }

    @Override
    public void refresh(TweetsItem item, int position, int totalCount, Object... payloads)
    {
        super.refresh(item, position, totalCount, payloads);

        CommonUtil.i("item.getPhotoUrls().lenght="+item.getPhotoUrls().length,"posi="+position);
        mPv.setPictures(item.getPhotoUrls());
//        mContentTv.setText("item.getPhotoUrls().lenght="+item.getPhotoUrls().length);
        mContentTv.setText(item.getContent());
        mCommentView.setComments(item.getComments());
        mNickNameTv.setText(item.getSenderInfo().getNickName());
    }
}
