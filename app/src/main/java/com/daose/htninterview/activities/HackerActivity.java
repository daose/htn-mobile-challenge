package com.daose.htninterview.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.daose.htninterview.R;
import com.daose.htninterview.fragments.HackerFragment;
import com.daose.htninterview.fresco.FrescoTransform;
import com.daose.htninterview.helpers.Transition;
import com.daose.htninterview.models.Hacker;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;

public class HackerActivity extends AppCompatActivity implements
        HackerFragment.OnFragmentInteractionListener {

    private static final String MAP_TAG = "map";
    private static final String PROFILE_TAG = "profile";

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hacker);

        Realm realm = Realm.getDefaultInstance();
        Hacker hacker = realm.where(Hacker.class).equalTo(Hacker.PRIMARY_KEY, getIntent().getStringExtra(Hacker.PRIMARY_KEY)).findFirst();

        setupToolbar(hacker.getName(), hacker.getPicture());
        if(hacker.hasLocation()) {
            setupFab();
        }
        renderHackerFragment();

        realm.close();
    }

    private void setupFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder);
                if(currentFragment.getTag().equals(PROFILE_TAG)) {
                    fab.setImageResource(R.drawable.ic_profile);
                    renderMapFragment();
                } else {
                    fab.setImageResource(R.drawable.ic_location);
                    popMapFragment();
                }
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder) ;
                if(currentFragment.getTag().equals(PROFILE_TAG)) {
                    fab.setImageResource(R.drawable.ic_location);
                } else {
                    fab.setImageResource(R.drawable.ic_profile);
                }
            }
        });
    }

    private void popMapFragment() {
        Transition.hide(getCurrentFragment().getView(), new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                getCurrentFragment().getView().setVisibility(View.INVISIBLE);
                getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void renderMapFragment() {
        final SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                final View mapView = getCurrentFragment().getView();
                Transition.show(mapView);

                Realm realm = Realm.getDefaultInstance();
                Hacker hacker = realm.where(Hacker.class).equalTo(Hacker.PRIMARY_KEY, getIntent().getStringExtra(Hacker.PRIMARY_KEY)).findFirst();
                LatLng hackerLocation = new LatLng(hacker.getLatitude(), hacker.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(hackerLocation)).setTitle(hacker.getName());
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(hackerLocation));
                realm.close();
            }
        });

        getCurrentFragment().getView().setVisibility(View.INVISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, mapFragment, MAP_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void renderHackerFragment() {
        Realm realm = Realm.getDefaultInstance();
        Hacker hacker = realm.where(Hacker.class).equalTo(Hacker.PRIMARY_KEY, getIntent().getStringExtra(Hacker.PRIMARY_KEY)).findFirst();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, HackerFragment.newInstance(hacker.getPrimaryValue()), PROFILE_TAG);
        ft.commit();

        realm.close();
    }

    private void setupToolbar(String name, String url) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);

        SimpleDraweeView blur = (SimpleDraweeView) findViewById(R.id.profile_blur);
        FrescoTransform.blur(this, blur, url);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_placeholder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEmailClick(String email){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email)));
    }

    @Override
    public void onPhoneClick(String number){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + number)));
    }
}
