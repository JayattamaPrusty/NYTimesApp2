package com.promobi.nyt.nytimesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;


import com.promobi.nyt.nytimesapp.R;
import com.promobi.nyt.nytimesapp.model.SearchFilter;

import java.util.ArrayList;

/**
 * Created by barbara on 2/10/16.
 */
public class SettingsFragment extends DialogFragment {

    public interface OnSettingsChangeListener {
        public void saveSearchFilterSettings(SearchFilter newSearchFilter);
    }

    private SearchFilter searchFilter;
    private OnSettingsChangeListener settingsChangeListener;
    private TextView tvBeginDate;
    private TextView tvClearDate;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(SearchFilter searchFilter) {
        SettingsFragment frag = new SettingsFragment();
        frag.setArguments(searchFilter.toBundle());
        return frag;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            settingsChangeListener = (OnSettingsChangeListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnSettingsChangeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.settings_title);
        return inflater.inflate(R.layout.settings, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // restore search filter from bundle
        searchFilter = new SearchFilter(getArguments());

        ArrayList<String> sortOrderOptions = new ArrayList<String>();
        sortOrderOptions.add(SearchFilter.SORT_ORDER_DEFAULT);
        sortOrderOptions.add(SearchFilter.SORT_ORDER_NEWEST);
        sortOrderOptions.add(SearchFilter.SORT_ORDER_OLDEST);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, sortOrderOptions);

        Spinner sSortOrder = (Spinner) view.findViewById(R.id.sSortOrder);
        if (searchFilter.getSortOrder() != null) {
            sSortOrder.setSelection(adapter.getPosition(searchFilter.getSortOrder()));
        }
        sSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                searchFilter.setSortOrder(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        tvBeginDate = (TextView) view.findViewById(R.id.tvBeginDate);
        tvBeginDate.setText(searchFilter.getBeginDate(SearchFilter.FORMAT_MONTH_DAY_YEAR));
        tvBeginDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle b = new Bundle();
                if (searchFilter.getBeginDateTimestamp() != null) {
                    b.putLong("beginDateTimestamp", searchFilter.getBeginDateTimestamp());
                }
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.setArguments(b);
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });

        tvClearDate = (TextView) view.findViewById(R.id.tvClearDate);
        tvClearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFilter.clearBeginDate();
                tvBeginDate.setText("");
                tvClearDate.setVisibility(View.INVISIBLE);
            }
        });

        if (searchFilter.getBeginDateTimestamp() != null) {
            tvClearDate.setVisibility(View.VISIBLE);
        }

        CheckBox cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbArts.setChecked(searchFilter.hasTopicChecked(SearchFilter.NEWS_DESK_ARTS));
        cbArts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    searchFilter.addNewsDeskTopic(SearchFilter.NEWS_DESK_ARTS);
                } else {
                    searchFilter.removeNewsDeskTopic(SearchFilter.NEWS_DESK_ARTS);
                }
            }
        });

        CheckBox cbFashionAndStyle = (CheckBox) view.findViewById(R.id.cbFashionAndStyle);
        cbFashionAndStyle.setChecked(searchFilter.hasTopicChecked(SearchFilter.NEWS_DESK_FASHION_AND_STYLE));
        cbFashionAndStyle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    searchFilter.addNewsDeskTopic(SearchFilter.NEWS_DESK_FASHION_AND_STYLE);
                } else {
                    searchFilter.removeNewsDeskTopic(SearchFilter.NEWS_DESK_FASHION_AND_STYLE);
                }
            }
        });

        CheckBox cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        cbSports.setChecked(searchFilter.hasTopicChecked(SearchFilter.NEWS_DESK_SPORTS));
        cbSports.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    searchFilter.addNewsDeskTopic(SearchFilter.NEWS_DESK_SPORTS);
                } else {
                    searchFilter.removeNewsDeskTopic(SearchFilter.NEWS_DESK_SPORTS);
                }
            }
        });

        Button btnSave = (Button) view.findViewById(R.id.btnSaveSettings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsChangeListener.saveSearchFilterSettings(searchFilter);
                getDialog().dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancelSettings);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    public void populateSetDate(int year, int month, int day) {
        searchFilter.setBeginDate(year, month, day);
        tvBeginDate.setText(searchFilter.getBeginDate(SearchFilter.FORMAT_MONTH_DAY_YEAR));
        tvClearDate.setVisibility(View.VISIBLE);
    }

}
