package com.daose.htninterview.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daose.htninterview.R;
import com.daose.htninterview.helpers.HackTheNorth;
import com.daose.htninterview.models.Hacker;
import com.daose.htninterview.models.Skill;
import com.db.chart.animation.Animation;
import com.db.chart.model.Bar;
import com.db.chart.model.BarSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.renderer.XRenderer;
import com.db.chart.view.HorizontalBarChartView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.realm.Realm;
import io.realm.Sort;

public class HackerFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private Realm realm;
    private String primaryValue;
    private Hacker hacker;

    private HorizontalBarChartView chart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            primaryValue = getArguments().getString(Hacker.PRIMARY_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        hacker = realm.where(Hacker.class).equalTo(Hacker.PRIMARY_KEY, primaryValue).findFirst();
        View rootView = inflater.inflate(R.layout.fragment_hacker, container, false);

        final TextView email = (TextView) rootView.findViewById(R.id.mail);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEmailClick(email.getText().toString());
            }
        });
        email.setText(hacker.getEmail());

        if(!hacker.getCompany().isEmpty()) {
            View companyView = rootView.findViewById(R.id.container_company);
            companyView.setVisibility(View.VISIBLE);

            TextView company = (TextView) rootView.findViewById(R.id.company);
            company.setText(hacker.getCompany());
        }

        if(!hacker.getPhone().isEmpty()) {
            View phoneView = rootView.findViewById(R.id.container_phone);
            phoneView.setVisibility(View.VISIBLE);

            final TextView phone = (TextView) rootView.findViewById(R.id.phone);
            phone.setText(hacker.getPhone());
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPhoneClick(phone.getText().toString());
                }
            });
        }

        chart = (HorizontalBarChartView) rootView.findViewById(R.id.chart_skills);
        setupChart();
        return rootView;
    }

    private void setupChart() {
        Map<String, Float> skillMap = HackTheNorth.getValidatedSkills(hacker.getSkills());
        List<Skill> allSkills = realm.where(Skill.class).distinct(Skill.NAME).sort(Skill.NAME, Sort.DESCENDING);

        BarSet barSet = new BarSet();
        for(Skill skill : allSkills) {
            TypedArray colors = getResources().obtainTypedArray(R.array.initial_colors);
            Bar bar;
            if(skillMap.containsKey(skill.name)) {
               bar = new Bar(skill.name, skillMap.get(skill.name) / Skill.MAX);
            } else {
               bar = new Bar(skill.name, 0);
            }
            bar.setColor(colors.getColor(Math.abs(skill.name.hashCode()) % 8, Color.BLACK));
            barSet.addBar(bar);
            colors.recycle();
        }

        final int[] order = new int[allSkills.size()];
        int row = 0;
        for(int i = order.length - 1; i >= 0; i--){
            order[i] = row;
            row++;
        }

        chart.addData(barSet);
        chart.setBarSpacing(getResources().getDimensionPixelSize(R.dimen.chart_bar_spacing));
        chart.setBorderSpacing(0)
                .setXAxis(false)
                .setYAxis(false)
                .setLabelsColor(getResources().getColor(R.color.colorTextSubtitle))
                .setXLabels(XRenderer.LabelPosition.NONE);
        chart.show(new Animation().setOverlap(.5f, order).setDuration(2000)) ;
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
        void onPhoneClick(String phoneNumber);
        void onEmailClick(String email);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(realm != null) {
            realm.close();
        }
    }

    public HackerFragment() {}

    public static HackerFragment newInstance(String primaryValue) {
        HackerFragment fragment = new HackerFragment();
        Bundle args = new Bundle();
        args.putString(Hacker.PRIMARY_KEY, primaryValue);
        fragment.setArguments(args);
        return fragment;
    }
}
