package com.ixbiopharma.glucose.progress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.ProgressFilterTypeView;
import com.ixbiopharma.glucose.component.TabDelegate;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.menu.MenuResultDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ProgressActivity
 * <p>
 * Created by ivan on 17.04.17.
 */
public class ProgressActivity extends BaseActivity {

    @BindView(R.id.tab)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.progress_filter)
    ProgressFilterTypeView progressFilterTypeView;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.arrow)
    View arrow;

    private int TYPE = ProgressFragment.WEIGHT;
    private int TIME = ProgressFragment.DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);

        toolbarView.showHomeArrow(true);
        toolbarView.setHomeArrowIcon(R.drawable.ic_menu);
        toolbarView.showAction(false);
        toolbarView.setHomeClickListener(v -> showMenu(1));
        toolbarView.setTitle("");

        new TabDelegate().init(tabLayout, position -> MenuResultDelegate.handleMenuResult(ProgressActivity.this, position), 1);

        progressFilterTypeView.setListener(position -> {
            if (position == 0) {
                TYPE = ProgressFragment.WEIGHT;
            } else if (position == 1) {
                TYPE = ProgressFragment.GLUCOSE;
            } else {
                TYPE = ProgressFragment.ACTIVITY;
            }

            showFragment();
        });

        arrow.setOnClickListener(v -> spinner.performClick());

        String[] items = getResources().getStringArray(R.array.progress_time);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.progress_spinner_item, items);

        spinner.setAdapter(adapter);
        spinner.setSelection(1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    TIME = ProgressFragment.DAY;
                } else if (position == 1) {
                    TIME = ProgressFragment.WEEK;
                } else {
                    TIME = ProgressFragment.YEAR;
                }

                showFragment();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showFragment() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, ProgressFragment.newInstance(TIME, TYPE))
                .commit();
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, ProgressActivity.class));
    }
}