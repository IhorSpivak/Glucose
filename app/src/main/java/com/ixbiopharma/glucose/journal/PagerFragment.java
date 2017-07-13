package com.ixbiopharma.glucose.journal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixbiopharma.glucose.R;
import com.ixbiopharma.glucose.model.DataType;
import com.ixbiopharma.glucose.model.Exercise;
import com.ixbiopharma.glucose.model.Type;
import com.ixbiopharma.glucose.utils.DataTypeUtils;
import com.ixbiopharma.glucose.utils.GlucoseUtils;
import com.ixbiopharma.glucose.utils.StringUtils;
import com.ixbiopharma.glucose.utils.TimeUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Pager item Fragment
 * <p>
 * Created by ivan on 17.04.17.
 */

public class PagerFragment extends Fragment {

    public static final String VALUE = "VALUE";
    public static final String TIME = "TIME";
    public static final String TYPE = "TYPE";
    public static final String IMAGE = "IMAGE";
    public static final String VALUE_TYPE = "VALUE_TYPE";
    public static final String COLOR = "COLOR";
    public static final String DATA_TYPE = "DATA_TYPE";


    public static PagerFragment newInstance(@NonNull DataType dataType) {

        Bundle args = new Bundle();

        if (dataType.getDataType() == Type.GLUCOSE) {
            args.putInt(COLOR, GlucoseUtils.getValueColor((float) dataType.getValue()));
        }

        args.putFloat(VALUE, (float) dataType.getValue());

        args.putString(VALUE_TYPE, DataTypeUtils.getValueType(dataType.getDataType()));

        args.putInt(IMAGE, DataTypeUtils.getTypeIcon(dataType));

        Calendar cal = Calendar.getInstance();
        cal.setTime(dataType.getDate());
        int day = cal.get(Calendar.DAY_OF_WEEK);

        args.putString(TIME, TimeUtils.getDayOfWeek(day));

        if (dataType.getDataType() == Type.EXERCISE) {
            args.putString(TYPE, ((Exercise) dataType).exercise);
        } else {
            args.putString(TYPE, DataTypeUtils.getTypeName(dataType));
        }

        args.putInt(DATA_TYPE, dataType.getDataType());

        PagerFragment fragment = new PagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.value)
    TextView value;
    @BindView(R.id.value_type)
    TextView value_type;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.image)
    ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        value.setTextColor(ContextCompat.getColor(getActivity(), getArguments().getInt(COLOR, R.color.gray)));

        value.setText(StringUtils.format(getArguments().getFloat(VALUE)));
        value_type.setText(getArguments().getString(VALUE_TYPE));
        day.setText(getArguments().getString(TIME));
        type.setText(getArguments().getString(TYPE));
        imageView.setImageResource(getArguments().getInt(IMAGE));

        view.setOnClickListener(view1 -> ((JournalActivity)getActivity()).hideButtons(view1));

    }
}
