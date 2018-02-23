package org.byters.bcphotoanimations.view.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.Injector;
import org.byters.bcphotoanimations.view.ui.adapters.AdapterProjects;

public class FragmentProjects extends FragmentBase {

    public static final int SPAN_NUM = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects, container, false);

        RecyclerView rvItems = view.findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), SPAN_NUM));
        rvItems.setAdapter(new AdapterProjects(Injector.getInstance().getPresenterAdapterProjects()));

        return view;
    }
}
