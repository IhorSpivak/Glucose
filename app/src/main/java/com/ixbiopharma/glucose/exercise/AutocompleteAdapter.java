package com.ixbiopharma.glucose.exercise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.ExerciseType;

import java.util.ArrayList;
import java.util.List;

/**
 * AutocompleteAdapter
 *
 * Created by ivan on 22.05.17.
 */

class AutocompleteAdapter  extends BaseAdapter implements Filterable {

    private List<ExerciseType> mResults;

    AutocompleteAdapter(List<ExerciseType> data) {
        mResults = new ArrayList<ExerciseType>();
        mResults.addAll(data);
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public ExerciseType getItem(int index) {
        return mResults.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.view_exercise_dropdown_item, parent, false);
        }

        ExerciseType exerciseType = getItem(position);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(exerciseType.getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<ExerciseType> books = findBooks(constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = books;
                    filterResults.count = books.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    mResults = (List<ExerciseType>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
    }

    /**
     * Returns a search result for the given book title.
     */
    private List<ExerciseType> findBooks(String bookTitle) {
        List<ExerciseType> res = new ArrayList<>();

        for (int i = 0; i < mResults.size(); i++) {
            ExerciseType exerciseType = mResults.get(i);
            if (exerciseType.getName().toLowerCase().contains(bookTitle.toLowerCase())){
                res.add(exerciseType);
            }
        }
        return res;
    }
}