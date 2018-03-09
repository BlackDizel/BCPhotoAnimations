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
import org.byters.bcphotoanimations.view.ui.adapters.AdapterFrames;

public class FragmentFrames extends FragmentBase
implements View.OnClickListener{

    private static final int SPAN_NUM = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frames, container, false);

        view.findViewById(R.id.ivPlay).setOnClickListener(this);
        setFrames(view);
        return view;
    }

    private void setFrames(View view) {
        RecyclerView rvItems = view.findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new GridLayoutManager(getContext(), SPAN_NUM));
        rvItems.setAdapter(new AdapterFrames(Injector.getInstance().getPresenterAdapterFrames()));
    }

    @Override
    public void onClick(View v) {
        //todo preview
    }

    //todo remove,copy,move, revert

    //todo export
}
