package com.ixbiopharma.glucose.menu;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.profile.ProfileActivity;
import com.ixbiopharma.glucose.search.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MenuFragment
 * <p>
 * Created by ivan on 14.05.17.
 */

public class MenuFragment extends Fragment {

    public static final int STORE = 3;
    public static final int NEWS = 2;
    public static final int PROGRESS = 1;
    public static final int JOURNAL = 0;

    public static MenuFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.search)
    EditText editText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                SearchActivity.start(getActivity(), editText.getText().toString());
            }
            return false;
        });

        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.gray));
        }

        int selected = getArguments().getInt("position", 0);

        view.findViewById(R.id.profile).setOnClickListener(v -> {
            ProfileActivity.start(getActivity());
            getActivity().finish();
        });

        view.findViewById(R.id.close).setOnClickListener(v -> getActivity().onBackPressed());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new MenuAdapter(pos -> {
            int result;
            if (pos == 1) {
                result = PROGRESS;
            } else if (pos == 0) {
                result = JOURNAL;
            } else if (pos == 2) {
                result = NEWS;
            } else {
                result = STORE;
            }

            MenuResultDelegate.handleMenuResult(getActivity(), result);
        }, selected));
    }

}
