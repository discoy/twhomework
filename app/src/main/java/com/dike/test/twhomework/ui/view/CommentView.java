package com.dike.test.twhomework.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dike.test.twhomework.R;
import com.dike.test.twhomework.entity.TweetsItem;
import com.dike.test.twhomework.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class CommentView extends LinearLayout
{
    private static List<View> mViewPool = new ArrayList<>();
    private static int mMaxViewPoolSize = 30;

    public CommentView(Context context)
    {
        super(context);
        init();
    }

    public CommentView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        setOrientation(VERTICAL);
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
            return mViewPool.remove(mMaxViewPoolSize);
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

    public void setComments(TweetsItem.Comment[] comments)
    {
        if(null == comments || comments.length == 0)
        {
            return;
        }
        LayoutParams layoutParams;
        RowView rowView;
        View tempView;
        int marginTop = CommonUtil.dip2px(getContext(),4);
        for(int i = 0; i < comments.length; i++)
        {
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = marginTop;
            tempView = getViewFromPool();
            if(RowView.class.isInstance(tempView))
            {
                rowView = (RowView) tempView;
            }
            else
            {
                rowView = new RowView(getContext());
            }
            addView(rowView,layoutParams);
            rowView.setComment(comments[i]);
        }
    }


    private static class RowView extends RelativeLayout
    {
        private ImageView mAvatarIv;
        private TextView mNicknameTv;
        private TextView mContentTv;
        public RowView(Context context)
        {
            super(context);
            init();
        }

        private void init()
        {
            ImageView avatarIv = new ImageView(getContext());
            avatarIv.setId(R.id.comment_view_avatar_iv);
            int avatarWidth = CommonUtil.dip2px(getContext(),30);
            LayoutParams layoutParams = new LayoutParams(avatarWidth,avatarWidth);
            addView(avatarIv,layoutParams);
            mAvatarIv = avatarIv;

            TextView nickNameTv = new TextView(getContext());
            nickNameTv.setId(R.id.comment_view_content_tv);
            nickNameTv.setTextColor(Color.GRAY);
            nickNameTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,CommonUtil.dip2px(getContext(),12));
            nickNameTv.setId(R.id.comment_view_nickname_tv);
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RIGHT_OF,R.id.comment_view_avatar_iv);
            layoutParams.addRule(ALIGN_TOP,R.id.comment_view_avatar_iv);
            addView(nickNameTv,layoutParams);
            mNicknameTv = nickNameTv;

            TextView contentTv = new TextView(getContext());
            contentTv.setTextColor(Color.BLACK);
            contentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,CommonUtil.dip2px(getContext(),16));
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(ALIGN_LEFT,R.id.comment_view_nickname_tv);
            layoutParams.addRule(BELOW,R.id.comment_view_nickname_tv);
            layoutParams.topMargin = CommonUtil.dip2px(getContext(),2);
            addView(contentTv,layoutParams);
            mContentTv = contentTv;
        }

        public void setComment(TweetsItem.Comment comment)
        {
            if(null != comment)
            {
                mContentTv.setText(comment.getContent());
                mNicknameTv.setText(comment.getSenderNickName());
                Glide.with(this).load(R.mipmap.ic_launcher).into(mAvatarIv);
            }
        }
    }
}
