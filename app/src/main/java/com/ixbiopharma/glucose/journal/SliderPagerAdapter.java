package com.ixbiopharma.glucose.journal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ixbiopharma.glucose.model.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * SliderPagerAdapter
 *
 * Created by ivan on 22.04.17.
 */

class SliderPagerAdapter extends FragmentPagerAdapter {

    SliderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private List<DataType> dataTypes = new ArrayList<>();

    @Override
    public Fragment getItem(int position) {
        return PagerFragment.newInstance(dataTypes.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return dataTypes.size();
    }

    public void setData(List<DataType> dataList){
        dataTypes.clear();
        dataTypes.addAll(dataList);
        notifyDataSetChanged();
    }

}