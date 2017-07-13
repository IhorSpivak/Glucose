package com.ixbiopharma.glucose.journal;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.JournalAdvice;
import com.ixbiopharma.glucose.model.JournalHeader;
import com.ixbiopharma.glucose.model.JournalNewsWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * JournalAdapter
 * <p>
 * Created by ivan on 21.05.17.
 */

class JournalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> data = new ArrayList<>();
    private Context context;
    private JournalListener listener;
    private final int ITEM = 0;
    private final int ADVICE = 1;
    private final int NEWS = 2;
    private final int HEADER = 3;

    JournalAdapter(JournalListener listener) {
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view;
        switch (viewType) {
            case ADVICE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_advice_item, parent, false);
                return new AdapterVH.AdviceVH(view);

            case NEWS:
                RecyclerView recycler = new RecyclerView(context);
                recycler.setOverScrollMode(View.OVER_SCROLL_NEVER);
                recycler.setLayoutManager(
                        new LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL, false));

                return new AdapterVH.NewsVH(recycler);

            case HEADER:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_simple_header, parent, false);
                return new AdapterVH.HeaderVH(view);

            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_simple_item, parent, false);
                return new AdapterVH.ItemVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object dataObject = data.get(position);

        switch (getItemViewType(position)) {
            case ADVICE:
                ((AdapterVH.AdviceVH) holder).bind((JournalAdvice) dataObject);
                break;

            case NEWS:
                ((AdapterVH.NewsVH) holder).bind((JournalNewsWrapper) dataObject, context);
                break;

            case HEADER:
                ((AdapterVH.HeaderVH) holder).bind((JournalHeader) dataObject);
                break;

            default: {
                ((AdapterVH.ItemVH) holder).bind(context, (DataType) dataObject, listener);
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object object = data.get(position);

        if (object instanceof JournalHeader) {
            return HEADER;

        } else if (object instanceof JournalAdvice) {
            return ADVICE;

        } else if (object instanceof JournalNewsWrapper) {
            return NEWS;

        } else {
            return ITEM;
        }
    }

    public void setData(List<?> dataList) {
        data.clear();
        data.addAll(dataList);
        notifyDataSetChanged();
    }
}
