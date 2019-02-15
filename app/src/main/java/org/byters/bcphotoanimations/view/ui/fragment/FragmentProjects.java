package org.byters.bcphotoanimations.view.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.INavigator;
import org.byters.bcphotoanimations.view.ui.adapters.AdapterProjects;

import javax.inject.Inject;

public class FragmentProjects extends FragmentBase implements View.OnClickListener {

    public static final int SPAN_NUM = 2;

    @Inject
    INavigator navigator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationStopMotion.getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        RecyclerView rvItems = view.findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), SPAN_NUM, getOrientation(), false));
        rvItems.setAdapter(new AdapterProjects());

        view.findViewById(R.id.ivAbout).setOnClickListener(this);

        return view;
    }

    private int getOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                ? GridLayoutManager.VERTICAL
                : GridLayoutManager.HORIZONTAL;
    }

    @Override
    public void onClick(View v) {
        navigator.navigateAbout();
    }
}
