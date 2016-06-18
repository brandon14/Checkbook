package com.brandon14.checkbook.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.brandon14.checkbook.R;
import com.brandon14.checkbook.model.Account;
import com.brandon14.checkbook.model.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTransactionAddEditCallbacks} interface
 * to handle interaction events.
 * Use the {@link AddEditTransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditTransactionFragment extends Fragment {
    private static final String ARG_IS_EDIT = "arg_is_edit";
    private static final String ARG_ACCOUNT_ID = "arg_account_id";
    private static final String ARG_TRANSACTION_ID = "arg_transaction_id";
    private static final String ARG_TRANSACTION_POSITION = "arg_transaction_position";

    private boolean mIsEdit;
    private long mAccountId;
    private long mTransactionId;
    private long mTransactionPosition;

    private CharSequence mTitle;

    private OnTransactionAddEditCallbacks mCallback;

    public AddEditTransactionFragment() {
        // Required empty public constructor
    }

    public static AddEditTransactionFragment newInstance(boolean isEdit, long accountId, long transactionId, int transactionPosition) {
        AddEditTransactionFragment fragment = new AddEditTransactionFragment();

        Bundle args = new Bundle();

        args.putBoolean(ARG_IS_EDIT, isEdit);
        args.putLong(ARG_ACCOUNT_ID, accountId);
        args.putLong(ARG_TRANSACTION_ID, transactionId);
        args.putInt(ARG_TRANSACTION_POSITION, transactionPosition);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIsEdit = getArguments().getBoolean(ARG_IS_EDIT);
            mAccountId = getArguments().getLong(ARG_ACCOUNT_ID);
            mTransactionId = getArguments().getLong(ARG_TRANSACTION_ID);
            mTransactionPosition = getArguments().getInt(ARG_TRANSACTION_POSITION);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_edit_transaction, container, false);

        if (mIsEdit) {
            mTitle = getString(R.string.title_edit_transaction);
        } else {
            mTitle = getString(R.string.title_add_transaction);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Activity activity = getActivity();
        activity.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_edit_transaction, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finish();

                return true;
            case R.id.action_add_edit_transaction_save:
                if (mIsEdit) {
                } else {
                }

                finish();
                return true;
            case R.id.action_add_edit_transaction_cancel:
                finish();

                return true;
            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnTransactionAddEditCallbacks) {
            mCallback = (OnTransactionAddEditCallbacks) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTransactionAddEditCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallback = null;
    }

    private void finish() {
        View view = this.getView();

        if (view != null) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getActivity().onBackPressed();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTransactionAddEditCallbacks {
        void onTransactionAdded(Transaction transaction);
        void onTransactionUpdated(int position, Transaction transaction);
    }
}
