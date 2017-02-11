package com.daose.htninterview.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.daose.htninterview.R;
import com.daose.htninterview.adapters.HackerAdapter;
import com.daose.htninterview.listeners.OnHackerListClickListener;
import com.daose.htninterview.models.Hacker;
import com.lapism.searchview.SearchView;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.Sort;

public class HackerListFragment extends Fragment {
    private static final String TAG = HackerListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_hacker_list, container, false);
        RealmRecyclerView rv = (RealmRecyclerView) rootView.findViewById(R.id.recycler_view);
        rv.setAdapter(new HackerAdapter(getContext(), realm.where(Hacker.class).findAllSorted("name", Sort.ASCENDING), new OnHackerListClickListener() {
            @Override
            public void onClick(Hacker hacker) {
                mListener.onClick(hacker);
            }
        }));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onClick(Hacker hacker);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        realm.close();
    }

    public HackerListFragment() {}

    public static HackerListFragment newInstance() {
        return new HackerListFragment();
    }
}
