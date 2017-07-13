package com.ixbiopharma.glucose.journal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.ixbiopharma.glucose.BaseActivity;
import com.ixbiopharma.glucose.GlucoseApplication;
import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.ArcMenu;
import com.ixbiopharma.glucose.component.FilterTypeView;
import com.ixbiopharma.glucose.component.LinearAlert;
import com.ixbiopharma.glucose.component.TabDelegate;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.di.RepositoryComponent;
import com.ixbiopharma.glucose.exercise.ExerciseActivity;
import com.ixbiopharma.glucose.exercise.FitHelper;
import com.ixbiopharma.glucose.food.FoodActivity;
import com.ixbiopharma.glucose.food.FoodEditActivity;
import com.ixbiopharma.glucose.menu.MenuResultDelegate;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.Type;
import com.ixbiopharma.glucose.model.api.DayAdviceResponse;
import com.ixbiopharma.glucose.value_picker.TypeValuePickerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class JournalActivity extends BaseActivity
        implements
        JournalContract.View,
        FitHelper.Listener,
        JournalListener {

    private JournalContract.Presenter presenter;

    @BindView(R.id.arcMenu)
    ArcMenu arcMenu;

    @BindView(R.id.container)
    RelativeLayout container;

    @BindView(R.id.alert)
    LinearAlert linearAlert;

    @BindView(R.id.tab)
    TabLayout tabLayout;

    @BindView(R.id.filterView)
    FilterTypeView filterTypeView;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.indicator)
    CircleIndicator circleIndicator;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.main_content)
    NestedScrollView mainContent;

    @BindView(R.id.placeholder_rv)
    View placeholder_rv;

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, JournalActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        ButterKnife.bind(this);

        toolbarView.showHomeArrow(true);
        toolbarView.setHomeArrowIcon(R.drawable.ic_menu);
        toolbarView.showAction(false);
        toolbarView.setHomeClickListener(v -> showMenu(0));
        toolbarView.setTitle("Journal");

        if (GlucoseApplication.showPopup){
            linearAlert.setVisibility(View.VISIBLE);
        } else {
            linearAlert.setVisibility(View.GONE);
        }

        linearAlert.setOnCloseClickListener(v -> {
            linearAlert.setVisibility(View.GONE);
            GlucoseApplication.showPopup = false;
        });

        filterTypeView.setListener(filterListener);

        new TabDelegate()
                .init(tabLayout, position ->
                        MenuResultDelegate.handleMenuResult(JournalActivity.this, position), 0);

        presenter = new JournalPresenter(this,
                RepositoryComponent.provideNewsRepository(),
                RepositoryComponent.provideUserRepository(),
                RepositoryComponent.provideJournalRepository(),
                RepositoryComponent.provideExerciseRepository(),
                RepositoryComponent.provideFoodRepository(),
                RepositoryComponent.provideFWeightRepository(),
                RepositoryComponent.provideGlucoseRepository());

        linearAlert.setOnTouchListener((v, event) -> {
            hideButtons(v);
            return false;
        });

        mainContent.setOnTouchListener((v, event) -> {
            hideButtons(v);
            return false;
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        journalAdapter = new JournalAdapter(this);
        recyclerView.setAdapter(journalAdapter);

        FitHelper fitHelper = new FitHelper<>(this);

        recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (recyclerView.getAdapter().getItemCount() == 0) {
                    placeholder_rv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    placeholder_rv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> presenter.sync());
    }

    public void hideButtons(View v) {
        arcMenu.post(() -> {
            if (arcMenu.isMenuOpened()) {
                if (v.getId() != R.id.btnException &&
                        v.getId() != R.id.btnFood &&
                        v.getId() != R.id.btnGlucose &&
                        v.getId() != R.id.btnWeight) {
                    arcMenu.toggleMenu();
                }
            }
        });

    }

    @Override
    public void setInitValue(float value) {
        presenter.updateWalk(value);
    }

    private JournalAdapter journalAdapter;

    private FilterTypeView.Listener filterListener = position -> presenter.onFilterClick(position);

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void showDataList(@NonNull List<Object> list) {
        recyclerView.post(() -> journalAdapter.setData(list));
    }

    @OnClick(R.id.btnWeight)
    void onWeightClick() {
        arcMenu.toggleMenu();
        TypeValuePickerActivity.createWeight(this, 11);
    }

    @Override
    public void showAdvice(@NonNull String userName,
                           @NonNull DayAdviceResponse dayAdviceResponse) {

        linearAlert.setDescriptionText(userName, dayAdviceResponse);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.btnGlucose)
    void onGlucoseClick() {
        arcMenu.toggleMenu();
        TypeValuePickerActivity.createGlucose(this, 0);
    }

    @OnClick(R.id.btnException)
    void onExerciseClick() {
        arcMenu.toggleMenu();
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnFood)
    void onFoodClick() {
        arcMenu.toggleMenu();
        FoodActivity.start(this);
    }

    @Override
    public void showSliderData(@NonNull List<DataType> dataList) {
        if (!dataList.isEmpty()) {
            viewPager.setVisibility(View.VISIBLE);
            circleIndicator.setVisibility(View.VISIBLE);

            SliderPagerAdapter sliderAdapter = new SliderPagerAdapter(getSupportFragmentManager());
            sliderAdapter.setData(dataList);
            viewPager.setAdapter(sliderAdapter);
            circleIndicator.setViewPager(viewPager);

        } else {
            viewPager.setVisibility(View.GONE);
            circleIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    public void onJournalClick(DataType dataType) {
        if (dataType.getDataType() == Type.FOOD) {
            FoodEditActivity.start(this, (Food) dataType, true);
        } else if (dataType.getDataType() == Type.GLUCOSE){
            TypeValuePickerActivity.editGlucose(this, 0, dataType.getId());
        } else if (dataType.getDataType() == Type.WIGHT){
            TypeValuePickerActivity.editWeight(this, 0, dataType.getId());
        } else if (dataType.getDataType() == Type.EXERCISE){
            ExerciseActivity.editExercise(this, dataType.getId());
        }
    }

    @Override
    public void onDelete(DataType dataType) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    presenter.delete(dataType);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onEdit(DataType dataType) {
        if (dataType.getDataType() == Type.GLUCOSE) {
            TypeValuePickerActivity.editGlucose(this, 10, dataType.getId());
        } else if (dataType.getDataType() == Type.WIGHT) {
            TypeValuePickerActivity.editWeight(this, 10, dataType.getId());
        } else if (dataType.getDataType() == Type.EXERCISE) {
            ExerciseActivity.editExercise(this, dataType.getId());
        } else {
            FoodEditActivity.start(this, (Food) dataType, false);
        }
    }
}
