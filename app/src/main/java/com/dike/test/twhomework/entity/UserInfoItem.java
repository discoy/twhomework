package com.dike.test.twhomework.entity;

import com.dike.test.twhomework.ui.recyclerview.TItem;
import com.google.gson.annotations.SerializedName;

public class UserInfoItem extends TItem
{
    @SerializedName("profile-image")
    private String profileImg;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("nick")
    private String nickName;
    @SerializedName("username")
    private String userName;

    public String getProfileImg()
    {
        return profileImg;
    }

    public void setProfileImg(String profileImg)
    {
        this.profileImg = profileImg;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void copyFrom(UserInfoItem infoItem)
    {
        if(null == infoItem)
        {
            return;
        }
        this.userName = infoItem.userName;
        this.avatar = infoItem.avatar;
        this.nickName = infoItem.nickName;
        this.profileImg = infoItem.profileImg;
    }
}
