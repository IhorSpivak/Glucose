package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * EmptyView RecyclerView
 * <p>
 * Created by ivan on 23.04.17.
 */

public class EmptyViewRv extends RecyclerView {

    @Nullable
    private View emptyView;

    public EmptyViewRv(Context context) {
        this(context, null);
    }

    public EmptyViewRv(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyViewRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private AdapterDataObserver emptyObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null) {
                if (adapter.getItemCount() == 0) {
                    if (emptyView != null) {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                    setVisibility(View.GONE);
                } else {
                    if (emptyView != null) {
                        emptyView.setVisibility(View.GONE);
                    }
                    setVisibility(View.VISIBLE);
                }
            }

        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(emptyObserver);
        }

        emptyObserver.onChanged();
    }

    public void setEmptyView(@Nullable View emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

}
