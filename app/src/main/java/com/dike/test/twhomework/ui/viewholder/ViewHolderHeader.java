package com.dike.test.twhomework.ui.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dike.test.twhomework.R;
import com.dike.test.twhomework.domain.AsyImageLoaderWrapper;
import com.dike.test.twhomework.entity.UserInfoItem;
import com.dike.test.twhomework.ui.recyclerview.AViewHolder;

public class ViewHolderHeader extends AViewHolder<UserInfoItem>
{
    private TextView mNickNameTv;
    private ImageView mProfileIv;
    private ImageView mAvatarIv;
    @Override
    protected void onInitView(View content)
    {
        mNickNameTv = content.findViewById(R.id.id_viewholder_tweets_header_user_nickname_tv);
        mProfileIv = content.findViewById(R.id.id_viewholder_tweets_header_user_profile_iv);
        mAvatarIv = content.findViewById(R.id.id_viewholder_tweets_header_user_avatar_iv);
    }

    @Override
    protected View getContentView()
    {
        return null;
    }

    @Override
    protected int getContentLayoutId()
    {
        return R.layout.viewholder_tweets_header;
    }

    @Override
    public void refresh(UserInfoItem item, int position, int totalCount, Object... payloads)
    {
        super.refresh(item, position, totalCount, payloads);
        if(!TextUtils.isEmpty(item.getNickName()))
        {
            mNickNameTv.setText(item.getNickName());
        }

        AsyImageLoaderWrapper.displayImage(getHost(),item.getProfileImg(),mProfileIv);
        AsyImageLoaderWrapper.displayImage(getHost(),item.getAvatar(),mAvatarIv);
    }
}
