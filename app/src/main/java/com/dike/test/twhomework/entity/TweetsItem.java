package com.dike.test.twhomework.entity;

import android.text.TextUtils;

import com.dike.test.twhomework.ui.recyclerview.TItem;
import com.google.gson.annotations.SerializedName;

public class TweetsItem extends TItem
{

    public static class Comment
    {
        private String content;
        private UserInfoItem sender;

        public Comment()
        {

        }

        public UserInfoItem getSender()
        {
            return sender;
        }

        public void setSender(UserInfoItem sender)
        {
            this.sender = sender;
        }

        public String getSenderNickName()
        {
            return null == sender ? "" : sender.getNickName();
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }
    }

    public static class Image
    {
        private String url;

        public String getUrl()
        {
            return url;
        }
    }

    private String content;
    @SerializedName("images")
    private Image[] mPhotoImgs;
    @SerializedName("sender")
    private UserInfoItem mSenderInfo;
    @SerializedName("comments")
    private Comment[] mComments;

    public TweetsItem()
    {
        super();
        setViewType(1);
    }


    public void setPhotoImgs(Image[] mPhotoImgs)
    {
        this.mPhotoImgs = mPhotoImgs;
    }

    public String[] getPhotoUrls()
    {
        if(null == mPhotoImgs || 0 == mPhotoImgs.length)
        {
            return null;
        }
        String[] urls = new String[mPhotoImgs.length];
        for(int i = 0; i < mPhotoImgs.length; i++)
        {
            urls[i] = mPhotoImgs[i].url;
        }
        return urls;
    }


    public Comment[] getComments()
    {
        return mComments;
    }

    public void setComments(Comment[] mComments)
    {
        this.mComments = mComments;
    }

    public UserInfoItem getSenderInfo()
    {
        return mSenderInfo;
    }

    public boolean isValid()
    {
        return !TextUtils.isEmpty(content) && null != mPhotoImgs && mPhotoImgs.length > 0;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
