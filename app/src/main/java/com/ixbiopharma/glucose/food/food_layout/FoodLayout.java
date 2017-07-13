package com.ixbiopharma.glucose.food.food_layout;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.component.EmptyViewRv;
import com.ixbiopharma.glucose.model.Food;
import com.ixbiopharma.glucose.model.FoodType;
import com.ixbiopharma.glucose.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * TestActivity
 * <p>
 * Created by ivan on 23.04.17.
 */

public class FoodLayout extends FrameLayout {

    @BindView(R.id.item_rv)
    EmptyViewRv item_rv;

    @BindView(R.id.hint_rv)
    EmptyViewRv hint_rv;

    @BindView(R.id.empty_view)
    View emptyView;

    @BindView(R.id.edittText)
    EditText search;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.image_container)
    View imageContainer;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

    private List<Food> foodList = new ArrayList<>();
    private List<FoodType> allFoodList = new ArrayList<>();
    private FoodItemAdapter itemAdapter;
    private FoodHintAdapter foodHintAdapter;
    private ImageAdapter imageAdapter;

    public FoodLayout(@NonNull Context context) {
        this(context, null);
    }

    public FoodLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FoodLayout(@NonNull Context context,
                      @Nullable AttributeSet attrs,
                      @AttrRes int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.view_food_layout, this);
        ButterKnife.bind(this);
        item_rv.setLayoutManager(new LinearLayoutManager(context));

        itemAdapter = new FoodItemAdapter(foodList, itemListener);

        item_rv.setAdapter(itemAdapter);
        item_rv.setEmptyView(emptyView);

        hint_rv.setLayoutManager(new LinearLayoutManager(context));

        foodHintAdapter = new FoodHintAdapter(this::addHintItem);

        hint_rv.setAdapter(foodHintAdapter);

        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (!search.getText().toString().isEmpty()) {
                    addHintItem(search.getText().toString());
                }
                return true;
            }
            return false;
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                List<FoodType> list = new ArrayList<>(allFoodList);

                for (Iterator<FoodType> it = list.iterator(); it.hasNext(); ) {
                    FoodType item = it.next();
                    if (!item.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        it.remove();
                    }
                }

                foodHintAdapter.setData(list);

                if (s.toString().isEmpty()) {
                    hint_rv.setVisibility(GONE);
                } else {
                    hint_rv.setVisibility(VISIBLE);
                }
            }
        });

        imageAdapter = new ImageAdapter(foodList, imageContainer);
        viewPager.setAdapter(imageAdapter);
        indicator.setViewPager(viewPager);
    }

    private FoodItemListener itemListener = new FoodItemListener() {

        @Override
        public void onUploadClick(Food food) {
            foodThatWaitingImage = food;
            getCallback().startUpload();
        }

        @Override
        public void onRemoveClick(Food food) {
            foodList.remove(food);
            itemAdapter.notifyDataSetChanged();
            imageAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAddClick() {
            emptyView.setVisibility(View.VISIBLE);
            search.requestFocus();
        }
    };

    private void addHintItem(FoodType food) {
        AndroidUtils.hideKeyboard(getActivity());
        foodList.add(Food.fromType(food));

        itemAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();

        search.getText().clear();
        foodHintAdapter.clear();
    }

    private void addHintItem(String s) {
        AndroidUtils.hideKeyboard(getActivity());

        Food food = new Food();
        food.setName(s);

        foodList.add(food);

        itemAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();

        search.getText().clear();
        foodHintAdapter.clear();
    }

    private Food foodThatWaitingImage;

    public void onImageAdded(String image) {
        foodThatWaitingImage.uri = image;
        imageAdapter.notifyDataSetChanged();
        itemAdapter.notifyDataSetChanged();
        indicator.setViewPager(viewPager);
    }

    public void setFoodHintData(List<FoodType> foods) {
        allFoodList.clear();
        allFoodList.addAll(foods);
    }

    public List<Food> getFoods() {
        return foodList;
    }

    private UploadCallback getCallback() {
        return (UploadCallback) getActivity();
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public void addFood(Food food) {
        foodList.add(food);
        itemAdapter.setWithInfo(true);
        itemAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
    }

    public void disable() {
        itemAdapter.setWithoutEditInfo(true);
        itemAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
    }
}
