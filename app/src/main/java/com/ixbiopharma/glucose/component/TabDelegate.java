package com.ixbiopharma.glucose.component;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import com.ixbiopharma.glucose.R;

/**
 * TabDelegate
 * <p>
 * Created by ivan on 23.04.17.
 */

public class TabDelegate {

    private int tabSelectedIconColor;
    private int tabUnselectedIconColor;
    private TabListener tabListener;

    public void init(TabLayout tabLayout, TabListener tabListener, int selectedTab) {
        Context context = tabLayout.getContext();
        this.tabListener = tabListener;

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context, android.R.color.transparent));

        tabSelectedIconColor = ContextCompat.getColor(context, android.R.color.black);
        tabUnselectedIconColor = ContextCompat.getColor(context, R.color.menu_gray);

        tabLayout.addTab(createTab(tabLayout, R.drawable.ic_journal));
        tabLayout.addTab(createTab(tabLayout, R.drawable.ic_progress));
        tabLayout.addTab(createTab(tabLayout, R.drawable.ic_news));
        tabLayout.addTab(createTab(tabLayout, R.drawable.ic_store));

        tabLayout.addOnTabSelectedListener(tabSelectedListener);

        TabLayout.Tab tab = tabLayout.getTabAt(selectedTab);
        if (tab != null) {
            tab.select();
        }
    }

    private TabLayout.Tab createTab(TabLayout tabLayout, int icon) {
        TabLayout.Tab newTab = tabLayout.newTab();
        newTab.setIcon(icon);

        if (newTab.getIcon() != null) {
            newTab.getIcon().setColorFilter(tabUnselectedIconColor, PorterDuff.Mode.SRC_IN);
        }
        return newTab;
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (tab.getIcon() != null) {
                tab.getIcon().setColorFilter(tabSelectedIconColor, PorterDuff.Mode.SRC_IN);
            }

            tabListener.onTabClicked(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (tab.getIcon() != null) {
                tab.getIcon().setColorFilter(tabUnselectedIconColor, PorterDuff.Mode.SRC_IN);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            if (tab.getIcon() != null) {
                tab.getIcon().setColorFilter(tabSelectedIconColor, PorterDuff.Mode.SRC_IN);
            }

            tabListener.onTabClicked(tab.getPosition());
        }
    };

    public interface TabListener {
        void onTabClicked(int position);
    }
}
