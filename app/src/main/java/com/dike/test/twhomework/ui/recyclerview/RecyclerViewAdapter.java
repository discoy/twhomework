package com.dike.test.twhomework.ui.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<TRecyclerViewHolder>
{

    int ORIENTATION_HORIZONTAL = 1;
    int ORIENTATION_VERTIVAL = 2;

    private List<? extends TItem> mItem;
    private SparseArray<Class<? extends AViewHolder>> mViewHolders;
    private LayoutInflater mInflater;
    private int mOrientation = ORIENTATION_VERTIVAL;

    public RecyclerViewAdapter(Context context,List<? extends TItem> items,
            SparseArray<Class<? extends AViewHolder>> viewHolders)
    {
        this.mItem = items;
        this.mViewHolders = viewHolders;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {

        TRecyclerViewHolder holderHull = null;
        AViewHolder realViewHolder = null;
        try
        {
            realViewHolder = mViewHolders.get(viewType).newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(null != realViewHolder)
        {
            holderHull = new TRecyclerViewHolder(realViewHolder.getView(mInflater),realViewHolder);
            RecyclerView.LayoutParams lp;
            if(ORIENTATION_VERTIVAL == mOrientation)
            {
                lp = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            else
            {
                lp = new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
            holderHull.getContentView().setLayoutParams(lp);
        }
        else
        {
            throw new IllegalArgumentException("The viewType["+viewType+"] not found,Please make sure " +
                    "you put the viewType["+viewType+"] into adapter ViewHolder map");
        }
        return holderHull;
    }

    @Override
    public int getItemViewType(int position)
    {
        TItem item = getItem(position);
        return null == item ? super.getItemViewType(position) : item.getViewType();
    }

    @Override
    public void onBindViewHolder(TRecyclerViewHolder holder, int position)
    {
        onBindViewHolder(holder, position,null);
    }

    @Override
    public void onViewRecycled(@NonNull TRecyclerViewHolder holder)
    {
        super.onViewRecycled(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView)
    {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(TRecyclerViewHolder holder, int position, List<Object> payloads)
    {
        if(!getItem(position).isVisible())
        {
            holder.getContentView().getLayoutParams().height = 0;
        }
        else
        {
            holder.getContentView().getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.getHolderCore().refresh(getItem(position),position,getItemCount(),payloads);
        }
    }


    public TItem getItem(int position)
    {
        if(0 <= position && getItemCount() > position)
        {
            return mItem.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount()
    {
        return null == mItem ? 0 : mItem.size();
    }
}
