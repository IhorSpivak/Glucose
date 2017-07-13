package com.ixbiopharma.glucose.menu;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;

/**
 * MenuAdapter
 * <p>
 * Created by ivan on 09.04.17.
 */

class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

    private Context context;
    private MenuListener menuListener;
    private int selected = 0;

    MenuAdapter(MenuListener menuListener, int selected) {
        this.menuListener = menuListener;
        this.selected = selected;
    }

    @Override
    public MenuAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu_item, parent, false);
        return new VH(v);
    }

    private int getColor(int color) {
        return ContextCompat.getColor(context, color);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.VH holder, int position) {
        if (position == selected) {
            holder.selector.setVisibility(View.VISIBLE);
            holder.text.setTextColor(getColor(android.R.color.white));
        } else {
            holder.selector.setVisibility(View.INVISIBLE);
            holder.text.setTextColor(getColor(R.color.light_gray3));
        }

        holder.text.setOnClickListener(v -> menuListener.onItemClick(position));

        switch (position) {
            case 0:
                holder.text.setText("Journal");
                break;
            case 1:
                holder.text.setText("My Progress");
                break;
            case 2:
                holder.text.setText("News");
                break;
            default:
                holder.text.setText("Store");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class VH extends RecyclerView.ViewHolder {

        private TextView text;
        private ImageView selector;

        VH(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            selector = (ImageView) itemView.findViewById(R.id.selector);
        }
    }

    interface MenuListener {

        void onItemClick(int pos);
    }
}
