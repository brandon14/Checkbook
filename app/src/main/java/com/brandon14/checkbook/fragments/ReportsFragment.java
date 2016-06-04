package com.brandon14.checkbook.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandon14.checkbook.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends Fragment {
    private static ReportsFragment sFragmentInstance;

    /**
     * * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReportsFragment.
     */
    public static ReportsFragment newInstance() {
        sFragmentInstance = sFragmentInstance == null ? new ReportsFragment() : sFragmentInstance;

        return sFragmentInstance;
    }

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getActivity().setTitle(getResources().getString(R.string.title_reports));
    }
}
