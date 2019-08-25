package com.dike.test.twhomework.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class TRecyclerViewHolder extends RecyclerView.ViewHolder
{
    private AViewHolder mHolderCore;

    public TRecyclerViewHolder(@NonNull View itemView,AViewHolder mHolderCore)
    {
        super(itemView);
    }

    public AViewHolder getHolderCore()
    {
        return mHolderCore;
    }

    public View getContentView()
    {
        return itemView;
    }
}
