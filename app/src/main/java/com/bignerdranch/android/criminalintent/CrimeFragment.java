package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by Ybr on 20.02.2018.
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String CRIME_WAS_CHANGED = "was_changed";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private boolean mCrimeWasChanged = false;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static boolean wasCrimeChanged(Intent result){
        return result.getBooleanExtra(CRIME_WAS_CHANGED, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        if(savedInstanceState != null){
            mCrimeWasChanged = savedInstanceState.getBoolean(CRIME_WAS_CHANGED);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //there is nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                setWasChangedResult(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //and there
            }
        });



        mDateButton = (Button)v.findViewById(R.id.crime_date);
        mDateButton.setText(DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate()));
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
                setWasChangedResult(true);
            }
        });

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CRIME_WAS_CHANGED, mCrimeWasChanged);
    }

    private void setWasChangedResult(boolean result){
        Intent data = new Intent();
        data.putExtra(CRIME_WAS_CHANGED, result);
        getActivity().setResult(Activity.RESULT_OK, data);
        mCrimeWasChanged = result;
    }
}
