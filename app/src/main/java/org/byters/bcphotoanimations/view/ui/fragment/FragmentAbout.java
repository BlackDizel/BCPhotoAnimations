package org.byters.bcphotoanimations.view.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.BuildConfig;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.INavigator;

import javax.inject.Inject;

public class FragmentAbout extends FragmentBase implements View.OnClickListener {

    @Inject
    INavigator navigator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        view.findViewById(R.id.tvFeedback).setOnClickListener(this);
        ((TextView) view.findViewById(R.id.tvVersion)).setText(String.format("%s(%s)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
    }

    @Override
    public void onClick(View v) {
        navigator.navigateFeedback(getContext());
    }
}
