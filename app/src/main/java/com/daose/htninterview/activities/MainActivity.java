package com.daose.htninterview.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.daose.htninterview.R;
import com.daose.htninterview.fragments.HackerListFragment;
import com.daose.htninterview.helpers.HackTheNorth;
import com.daose.htninterview.models.Hacker;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        SearchAdapter.OnItemClickListener,
        HackerListFragment.OnFragmentInteractionListener {

    private SearchAdapter searchAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HackTheNorth.updateUsersAsync();
        setupSearchView();
        renderHackerListFragment();
    }

    private void renderHackerListFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, HackerListFragment.newInstance());
        ft.commit();
    }

    private void setupSearchView() {
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setVersion(SearchView.VERSION_TOOLBAR);
        searchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
        searchView.setHint(getString(R.string.htn));
        searchView.setDivider(false);
        searchView.setVoice(false);
        searchView.setNavigationIcon(R.drawable.ic_search);
        searchView.setOnQueryTextListener(this);

        searchAdapter = new SearchAdapter(this, new ArrayList<SearchItem>());
        searchAdapter.addOnItemClickListener(this);
        searchView.setAdapter(searchAdapter);
    }

    @Override
    public void onClick(Hacker hacker) {
        Intent intent = new Intent(this, HackerActivity.class);
        intent.putExtra(Hacker.PRIMARY_KEY, hacker.getPrimaryValue());
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean onQueryTextChange(String newText) {
        if (newText.length() < 1) {
            int items = searchAdapter.getSuggestionsList().size();
            searchAdapter.getSuggestionsList().clear();
            searchAdapter.notifyItemRangeRemoved(0, items);
            return false;
        }

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Hacker> queryResult = realm.where(Hacker.class).contains(Hacker.NAME, newText, Case.INSENSITIVE).findAll();
        int prevSize = searchAdapter.getSuggestionsList().size();
        int querySize = queryResult.size();
        int minSize = Math.min(prevSize, querySize);
        for (int i = 0; i < minSize; i++) {
            if (!searchAdapter.getSuggestionsList().get(i).equals(queryResult.get(i).getName())) {
                searchAdapter.getSuggestionsList().set(i, new SearchItem(queryResult.get(i).getName()));
                searchAdapter.notifyItemChanged(i);
            }
        }
        if (querySize > prevSize) {
            for (int i = minSize; i < querySize; i++) {
                searchAdapter.getSuggestionsList().add(new SearchItem(queryResult.get(i).getName()));
            }
            searchAdapter.notifyItemRangeInserted(minSize, querySize - minSize);
        } else {
            for (int i = minSize; i < prevSize; i++) {
                searchAdapter.getSuggestionsList().remove(minSize);
            }
            searchAdapter.notifyItemRangeRemoved(minSize, prevSize - minSize);
        }
        realm.close();
        return false;
    }

    @Override
    public void onItemClick(View view, int position) {
        String name = searchAdapter.getSuggestionsList().get(position).get_text().toString();
        Realm realm = Realm.getDefaultInstance();
        try {
            Hacker hacker = realm.where(Hacker.class).equalTo(Hacker.NAME, name).findFirst();
            Intent intent = new Intent(this, HackerActivity.class);
            intent.putExtra(Hacker.PRIMARY_KEY, hacker.getPrimaryValue());
            startActivity(intent);
            searchView.close(true);
        } finally {
            realm.close();
        }
    }
}
