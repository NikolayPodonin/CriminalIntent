package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Ybr on 21.02.2018.
 */

public class CrimeListFragment extends Fragment {
    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mLastClickedPosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyItemChanged(mLastClickedPosition);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CRIME){
            if(data == null){
                return;
            }

            if(CrimeFragment.wasCrimeChanged(data)){
                updateUI();
            }
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTitleTextView;
        public TextView mDateTextView;
        public CheckBox mSolvedCheckBox;
        private Crime mCrime;
        private int mPosition = 0;

        public CrimeHolder(View itemView){
            super(itemView);
            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
            itemView.setOnClickListener(this);
        }

        public void bindCrime(Crime crime, int position){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getFormatingDate());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            mLastClickedPosition = mPosition;
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);

            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
