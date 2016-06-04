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
 * Use the {@link RecurringFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecurringFragment extends Fragment {
    private static RecurringFragment sFragmentInstance;
    /**
     * * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecurringFragment.
     */
    public static RecurringFragment newInstance() {
        sFragmentInstance = sFragmentInstance == null ? new RecurringFragment() : sFragmentInstance;

        return sFragmentInstance;
    }

    public RecurringFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recurring, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getActivity().setTitle(getResources().getString(R.string.title_recurring));
    }
}
